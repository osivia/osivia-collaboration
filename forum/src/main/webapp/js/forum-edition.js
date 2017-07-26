$JQry(function() {
    var $textarea = $JQry(".forum-edition textarea[name=message]"),
        textareaId = $textarea.attr("id"),
        loaded = $textarea.data("loaded");

    if (!loaded) {
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
    }


    if (tinymce.get(textareaId)) {
        if (!loaded) {
            tinymce.execCommand("mceRemoveEditor", true, textareaId);
            tinymce.execCommand("mceAddEditor", true, textareaId);
        }
    } else {
        tinymce.init({
            selector: ".forum-edition textarea[name=message]",
            language: "fr_FR",
            plugins: "autosave link lists paste osivia_link",

            branding: false,
            menubar: false,
            toolbar: "undo redo | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | bullist numlist | osivia_link",
            statusbar: false,

            element_format: "html",

            content_css: ["/osivia-portal-custom-web-assets/css/bootstrap/bootstrap.min.css"],
            body_class: "panel-body",
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