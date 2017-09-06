$JQry(function() {
    var $form = $JQry(".forum-edition form");

    
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
                plugins: "autosave link lists noneditable paste osivia_link",

                branding: false,
                menubar: false,
                toolbar: "undo redo | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | bullist numlist | osivia_link",
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

                content_css: ["/osivia-portal-custom-web-assets/css/bootstrap/bootstrap.min.css", "/osivia-services-forum/css/forum-tinymce.min.css"],
                body_class: "forum-mce-content-body",
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
        // Auto upload vignette for preview generation
        $JQry(".forum-edition input[type=file][name='vignette.upload']").change(function (event) {
            var $target = $JQry(event.target),
                $formGroup = $target.closest(".form-group"),
                $submit = $formGroup.find("input[type=submit][name='upload-vignette']");

            $submit.click();
        });


        // Auto upload attachment
        $JQry(".forum-edition input[type=file][name='attachments.upload']").change(function (event) {
            var $target = $JQry(event.target),
                $formGroup = $target.closest(".form-group"),
                $submit = $formGroup.find("input[type=submit][name='upload-attachment']");

            $submit.click();
        });


        // Delete attachment
        $JQry(".forum-edition button[name='delete-attachment']").click(function (event) {
            var $target = $JQry(event.target),
                $button = $target.closest("button"),
                index = $button.data("index"),
                $formGroup = $button.closest(".form-group"),
                $hidden = $formGroup.find("input[type=hidden][name='attachments.deletedIndex']"),
                $submit = $formGroup.find("input[type=submit][name='delete-attachment']");

            $hidden.val(index);

            $submit.click();
        });


        // Image error message
        $JQry(".forum-edition img").on("error", function (event) {
            var $target = $JQry(event.target),
                message = $target.data("error-message"),
                $message;

            if (message) {
                $message = $JQry(document.createElement("span"));
                $message.text(message);

                $target.replaceWith($message);
            }
        });


        $form.data("loaded", true);
    }
});