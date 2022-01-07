$JQry(function() {
    var $form = $JQry(".forum-edition form");

    
    $JQry(".forum textarea[data-editor=message]").each(function(index, element) {
        var $textarea = $JQry(element);
        var id = $textarea.attr("id");
        var editorUrl = $textarea.data("editor-url");

        if (editorUrl === undefined) {
            console.error("Editor URL is undefined.")
        } else if (tinymce.get(id)) {
            if (!$textarea.data("loaded")) {
                tinymce.execCommand("mceRemoveEditor", true, id);
                tinymce.execCommand("mceAddEditor", true, id);
            }
        } else {
            tinymce.init({
                selector: ".forum textarea[data-editor=message]",
                language: "fr_FR",
                plugins: "autosave lists noneditable paste",
                external_plugins: {
                    "osivia-link": "/osivia-services-editor-helpers/js/link/plugin.min.js"
                },

                branding: false,
                menubar: false,
                toolbar: "undo redo | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | bullist numlist | osivia-link",
                statusbar: false,

                schema: "html5",
                element_format : "html",
                formats: {
                    alignleft: {
                        selector: "p,ul,ol,li",
                        classes: "text-left"
                    },
                    aligncenter: {
                        selector: "p,ul,ol,li",
                        classes: "text-center"
                    },
                    alignright: {
                        selector: "p,ul,ol,li",
                        classes: "text-right"
                    },
                    alignjustify: {
                        selector: "p,ul,ol,li",
                        classes: "text-justify"
                    },
                    bold: {
                        inline: "strong"
                    },
                    italic: {
                        inline: "em"
                    },
                    underline: {
                        inline: "u",
                        exact: true
                    },
                    strikethrough: {
                        inline: "del"
                    }
                },

                browser_spellcheck: true,
                contextmenu: false,
                content_css: ["/osivia-portal-custom-web-assets/css/bootstrap.min.css", "/osivia-portal-custom-web-assets/css/osivia.min.css"],
                height: "250",

                convert_urls: false,
                paste_as_text: true,

                setup: function(editor) {
                    editor.on("change", function (e) {
                        editor.save();
                    });
                }
            });
        }

        $textarea.data("loaded", true);
    });
    
    
    if (!$form.data("loaded")) {
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


        $form.data("loaded", true);
    }
});
