tinymce.create("tinymce.plugins.OsiviaLink", {
    OsiviaLink: function(editor, url) {
        /**
         * Load modal.
         * @param event event
         */
        function loadModal(event) {
            var $textarea = $JQry("#" + $JQry.escapeSelector(editor.id)),
                data = {},
                selection = editor.selection, dom = editor.dom,
                selectedElm, anchorElm, initialText, onlyText;

            function isOnlyTextSelected(anchorElm) {
                var html = selection.getContent();

                // Partial html and not a fully selected anchor element
                if (/</.test(html) && (!/^<a [^>]+>[^<]+<\/a>$/.test(html) || html.indexOf("href=") == -1)) {
                    return false;
                }

                if (anchorElm) {
                    var nodes = anchorElm.childNodes, i;

                    if (nodes.length === 0) {
                        return false;
                    }

                    for (i = nodes.length - 1; i >= 0; i--) {
                        if (nodes[i].nodeType != 3) {
                            return false;
                        }
                    }
                }

                return true;
            }

            selectedElm = selection.getStart();
            anchorElm = dom.getParent(selectedElm, "a[href]");
            onlyText = isOnlyTextSelected(anchorElm);

            data.text = initialText =  anchorElm ? (anchorElm.innerText || anchorElm.textContent) : selection.getContent({format: "text"});
            data.href = anchorElm ? dom.getAttrib(anchorElm, "href") : "";

            if ((value = dom.getAttrib(anchorElm, "title"))) {
                data.title = value;
            }


            jQuery.ajax({
                url: $textarea.data("editor-url"),
                async: true,
                cache: true,
                headers: {
                    "Cache-Control": "max-age=86400, public"
                },
                data: {
                    editorId: "link",
                    url: data.href,
                    text: data.text,
                    title: data.title,
                    onlyText: onlyText
                },
                dataType: "text",
                success : function(data, status, xhr) {
                    var $modal = $JQry("#osivia-modal"),
                        json = JSON.parse(data);

                    $modal.data("load-url", json.url);
                    $modal.data("callback-function", "tinymceLinkModalCallback");
                    $modal.data("callback-function-args", new Array(editor.id, initialText, onlyText).join("|"));
                    $modal.data("title", json.title);
                    $modal.data("size", "large");

                    $modal.modal("show");
                }
            });
        }

        // Button
        editor.addButton("osivia_link", {
            text: "",
            icon: "link",
            tooltip: "Insert/edit link",
            stateSelector: "a[href]",
            onclick: loadModal,
            onpostrender: function (event) {

            }
        });
    }
});


tinymce.PluginManager.add("osivia_link", tinymce.plugins.OsiviaLink);


/**
 * TinyMCE link modal callback.
 *
 * @param arguments modal callback arguments
 */
function tinymceLinkModalCallback(arguments) {
    var array = arguments.split("|"),
        editorId = array[0], initialText = array[1], onlyText = array[2],
        editor = tinymce.EditorManager.get(editorId),
        $modal = $JQry("#osivia-modal"),
        $form = $modal.find("form"),
        $done = $form.find("input[name=done]"), done = $done.val(),
        $url = $form.find("input[name=url]"), url = $url.val(),
        $text = $form.find("input[name=text]"), text = $text.val(),
        $title = $form.find("input[name=title]"), title = $title.val(),
        selection = editor.selection, dom = editor.dom,
        selectedElm, anchorElm;

    selectedElm = selection.getNode();
    anchorElm = dom.getParent(selectedElm, "a[href]");


    function createLink() {
        var linkAttrs = {
            "href": url,
            "target": null,
            "rel": null,
            "class": null,
            "title": title ? title : null
        };

        if (anchorElm) {
            editor.focus();

            if (onlyText && text != initialText) {
                if ("innerText" in anchorElm) {
                    anchorElm.innerText = text;
                } else {
                    anchorElm.textContent = text;
                }
            }

            dom.setAttribs(anchorElm, linkAttrs);

            selection.select(anchorElm);
            editor.undoManager.add();
        } else {
            if (onlyText === "true") {
                editor.insertContent(dom.createHTML("a", linkAttrs, dom.encode(text)));
            } else {
                editor.execCommand("mceInsertLink", false, linkAttrs);
            }
        }
    }

    function insertLink() {
        editor.undoManager.transact(createLink);
    }

    function unlink() {
        editor.undoManager.transact(function() {
            editor.execCommand("unlink");
        });
    }


    if (done === "true") {
        if (url) {
            insertLink();
        } else {
            unlink();
        }
    }
}
