tinymce.PluginManager.add("osivia-link", function (editor, url) {

    var getAnchorElement = function (editor, selectedElm) {
        selectedElm = selectedElm || editor.selection.getNode();
        return editor.dom.getParent(selectedElm, "a[href]");
    };

    var isOnlyTextSelected = function (html) {
        if (/</.test(html) && (!/^<a [^>]+>[^<]+<\/a>$/.test(html) || html.indexOf('href=') === -1)) {
            return false;
        }
        return true;
    };

    var openModal = function openModal() {
        var $textarea = $JQry("#" + $JQry.escapeSelector(editor.id));
        var selectedElm = editor.selection.getNode();
        var anchorElm = getAnchorElement(editor, selectedElm);

        var text = anchorElm ? anchorElm.innerText || anchorElm.textContent : editor.selection.getContent({format: "text"}).replace(/\uFEFF/g, "");
        var onlyText = isOnlyTextSelected(editor.selection.getContent())

        jQuery.ajax({
            url: $textarea.data("editor-url"),
            async: true,
            cache: true,
            headers: {
                "Cache-Control": "max-age=86400, public"
            },
            data: {
                editorId: "link",
                url: anchorElm ? editor.dom.getAttrib(anchorElm, "href") : "",
                text: text,
                title: anchorElm ? editor.dom.getAttrib(anchorElm, "title") : "",
                onlyText: onlyText
            },
            dataType: "json",
            success : function(data, status, xhr) {
                var $modal = $JQry("#osivia-modal");

                $modal.data("load-url", data.url);
                $modal.data("callback-function", "tinymceLinkModalCallback");
                $modal.data("callback-function-args", [editor.id, text, onlyText].join("|"));
                $modal.data("title", data.title);
                $modal.data("size", "large");

                $modal.modal("show");
            }
        });
    };


    editor.ui.registry.addToggleButton("osivia-link", {
        icon: "link",
        tooltip: 'Insert/edit link',
        onAction: function () {
            openModal();
        },
        onSetup: function (api) {
            var nodeChangeHandler = function (e) {
                return api.setActive(!editor.mode.isReadOnly() && !!getAnchorElement(editor, e.element));
            };
            editor.on("NodeChange", nodeChangeHandler);
            return function () {
                return editor.off("NodeChange", nodeChangeHandler);
            };
        }
    });

});


/**
 * TinyMCE link modal callback.
 *
 * @param arguments modal callback arguments
 */
function tinymceLinkModalCallback(arguments) {
    var array = arguments.split("|");
    var editorId = array[0], initialText = array[1], onlyText = (array[2] === "true");
    var editor = tinymce.EditorManager.get(editorId);
    var selectedElm = editor.selection.getNode();
    var anchorElm = editor.dom.getParent(selectedElm, "a[href]");
    var $modal = $JQry("#osivia-modal");
    var $form = $modal.find("form");
    var $done = $form.find("input[name=done]"), done = ($done.val() === "true");
    var $url = $form.find("input[name=url]"), url = $url.val();
    var $text = $form.find("input[name=text]"), text = $text.val();
    var $title = $form.find("input[name=title]"), title = $title.val();


    if (done) {
        if (url) {
            var linkAttrs = {
                "href": url,
                "target": null,
                "rel": null,
                "class": null,
                "title": title
            };

            editor.undoManager.transact(function () {
                if (anchorElm) {
                    editor.focus();

                    if (onlyText && (text !== initialText)) {
                        if (anchorElm.hasOwnProperty("innerText")) {
                            anchorElm.innerText = text;
                        } else {
                            anchorElm.textContent = text;
                        }
                    }

                    editor.dom.setAttribs(anchorElm, linkAttrs);
                    editor.selection.select(anchorElm);
                } else {
                    if (onlyText) {
                        editor.insertContent(editor.dom.createHTML("a", linkAttrs, editor.dom.encode(text)));
                    } else {
                        editor.execCommand("mceInsertLink", false, linkAttrs);
                    }
                }
            });
        } else {
            // Unlink
            editor.undoManager.transact(function () {
                var node = editor.selection.getNode();
                var anchorElm = editor.dom.getParent(node, "a[href]", editor.getBody());
                if (anchorElm) {
                    editor.dom.remove(anchorElm, true);
                }
                editor.focus();
            });
        }
    }
}