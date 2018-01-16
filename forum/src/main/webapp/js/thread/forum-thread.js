$JQry(function () {
    var $form = $JQry(".forum-thread form");

    
    $JQry(".forum textarea[data-editor=message]").each(function(index, element) {
        var $textarea = $JQry(element),
            id = $textarea.attr("id");

        if (tinymce.get(id)) {
            if (!$textarea.data("loaded")) {
                tinymce.execCommand("mceRemoveEditor", true, id);
                tinymce.execCommand("mceAddEditor", true, id);
            }
        } else {
            tinymce.init({
                selector: ".forum textarea[data-editor=message]",
                language: "fr_FR",
                plugins: "autosave link lists noneditable paste",
                external_plugins: {
                	"osivia_link": "/osivia-services-editor-helpers/js/link/plugin.min.js"
                },

                branding: false,
                menubar: false,
                toolbar: "undo redo | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | bullist numlist | blockquote | osivia_link",
                statusbar: false,

                element_format: "html",
                formats: {
                    alignleft: {
                        selector: "p, ul, ol, li",
                        classes: "text-left"
                    },
                    aligncenter: {
                        selector: "p, ul, ol, li",
                        classes: "text-center"
                    },
                    alignright: {
                        selector: "p, ul, ol, li",
                        classes: "text-right"
                    },
                    alignjustify: {
                        selector: "p, ul, ol, li",
                        classes: "text-justify"
                    },
                    bold: {
                        inline: "strong"
                    },
                    italic: {
                        inline: "em"
                    },
                    underline: {
                        inline: "u"
                    },
                    strikethrough: {
                        inline: "s"
                    }
                },

                content_css: ["/osivia-portal-custom-web-assets/css/bootstrap.min.css", "/osivia-services-forum/css/forum-tinymce.min.css"],
                height: 200,

                // Prevent relative URL conversion
                convert_urls: false,
                // Remove style on paste
                paste_as_text: true,

                browser_spellcheck: true
            });
        }

        $textarea.data("loaded", true);
    });
    
    
    if (!$form.data("loaded")) {
        // Quote post
        $JQry(".forum-thread button[name='quote-post']").click(function (event) {
            var $target = $JQry(event.target),
                $formGroup = $target.closest(".form-group"),
                id = $formGroup.attr("id"),
                $form = $formGroup.closest("form"),
                userDisplayName = $formGroup.find(".user-display-name").text(),
                $message = $formGroup.find(".forum-thread-message").children().clone(),
                $thread = $formGroup.closest(".forum-thread"),
                $textarea, editor, dom, blockquote, blockquoteHeader, blockquoteHeaderLink;

            $textarea = $thread.find("textarea[name='posts.editedPost.message']");
            if (!$textarea.length) {
                $textarea = $thread.find("textarea[name='reply.message']");
            }
            editor = tinymce.EditorManager.get($textarea.attr("id"));


            jQuery.ajax({
                url: $form.data("quote-url"),
                data: {
                    id: id
                },
                dataType: "html",
                success : function(data, status, xhr) {
                    // Blockquote
                    editor.insertContent(data);

                    // Scroll to editor
                    editor.editorContainer.scrollIntoView();
                }
            });
        });


        // Edit post
        $JQry(".forum-thread button[name='edit-post']").click(function (event) {
            var $target = $JQry(event.target),
                $button = $target.closest("button"),
                $formGroup = $button.closest(".form-group"),
                id = $formGroup.attr("id"),
                $input = $JQry($button.data("input")),
                $submit = $JQry($button.data("submit"));

            $input.val(id);

            $submit.click();
        });


        // Delete post
        $JQry(".forum-thread button[name='delete-post']").click(function (event) {
            var $target = $JQry(event.target),
                $button = $target.closest("button"),
                $formGroup = $button.closest(".form-group"),
                id = $formGroup.attr("id"),
                $input = $JQry($button.data("input")),
                $modal = $JQry($button.data("modal"));

            $input.val(id);

            $modal.modal("show");
        });


        // Auto upload attachment
        $JQry(".forum-thread input[type=file]").change(function (event) {
            var $target = $JQry(event.target),
                $submit = $JQry($target.data("submit"));

            $submit.click();
        });


        // Delete attachment
        $JQry(".forum-thread button[name='delete-attachment']").click(function (event) {
            var $target = $JQry(event.target),
                $button = $target.closest("button"),
                index = $button.data("index"),
                $input = $JQry($button.data("input")),
                $submit = $JQry($button.data("submit"));

            $input.val(index);

            $submit.click();
        });


        $form.data("loaded", true);
    }
});
