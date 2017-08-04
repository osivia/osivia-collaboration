$JQry(function() {
    $JQry(".forum textarea[data-editor=message]").each(function(index, element) {
        var $textarea = $JQry(element),
            id = $textarea.attr("id"),
            loaded = $textarea.data("loaded");

        if (tinymce.get(id)) {
            if (!loaded) {
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
});
