tinymce.PluginManager.add("osivia-image", function (editor, url) {

    var openModal = function openModal() {
        var $textarea = $JQry("#" + $JQry.escapeSelector(editor.id));
        var image = editor.selection.getNode();

        jQuery.ajax({
            url: $textarea.data("editor-url"),
            async: true,
            cache: true,
            headers: {
                "Cache-Control": "max-age=86400, public"
            },
            data: {
                editorId: "image",
                src: image ? editor.dom.getAttrib(image, "src") : "",
                alt: image ? editor.dom.getAttrib(image, "alt") : "",
            },
            dataType: "json",
            success: function (data, status, xhr) {
                var $modal = $JQry("#osivia-modal");

                $modal.data("load-url", data.url);
                $modal.data("callback-function", "tinymceImageModalCallback");
                $modal.data("callback-function-args", editor.id);
                $modal.data("title", data.title);

                $modal.modal("show");
            }
        });
    };


    editor.ui.registry.addToggleButton("osivia-image", {
        icon: "image",
        tooltip: 'Insert/edit image',
        onAction: function () {
            openModal();
        },
        onSetup: function (api) {
            return editor.selection.selectorChangedWithUnbind('img:not([data-mce-object],[data-mce-placeholder]),figure.image', api.setActive).unbind;
        }
    });


    editor.on("BeforeSetContent", function(content, format, paste, selection) {
        console.log("[plugin] BeforeSetContent"); // FIXME

        var $textarea = $JQry("#" + $JQry.escapeSelector(editor.id));
        var editorUrl = $textarea.data("editor-url");

        var $content = $JQry("<div/>").html(content.content);
        $content.find("img[src]").each(function(index, element) {
            var $image = $JQry(element);
            var src = $image.attr("src");
            if (src.startsWith("/nuxeo")) {
                $image.attr("data-mce-src", src);
                $image.attr("src", editorUrl + "&editorId=image&editorResourceId=preview&src=" + src);
            }
        });

        content.content = $content.html();
    });

});


/**
 * TinyMCE image modal callback.
 *
 * @param arguments modal callback arguments
 */
function tinymceImageModalCallback(arguments) {
    var array = arguments.split("|");
    var editorId = array[0];

    var editor = tinymce.EditorManager.get(editorId);
    var selectedElement = editor.selection.getNode();
    var image = selectedElement && selectedElement.nodeName === "IMG" ? selectedElement : null;

    var $modal = $JQry("#osivia-modal");
    var $form = $modal.find("form");
    var $done = $form.find("input[name=done]"), done = ($done.val() === "true");
    var $url = $form.find("input[name=url]"), url = $url.val();
    var $alt = $form.find("input[name=alt]"), alt = $alt.val();
    var $originalUrl = $form.find("input[name=originalUrl]"), originalUrl = $originalUrl.val();

    if (done) {
        var attribs = {
            "src": url,
            "alt": alt,
        };

        editor.undoManager.transact(function() {
            if (image) {
                if (url) {
                    editor.dom.setAttribs(image, attribs);
                    editor.selection.select(image);
                } else {
                    editor.dom.remove(image);
                    editor.focus();
                    editor.nodeChanged();
                }
            } else if (url) {
                editor.insertContent(editor.dom.createHTML("img", attribs));
            }
        });
    }
}
