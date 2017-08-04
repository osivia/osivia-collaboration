$JQry(function() {
    var $form = $JQry(".forum-edition form"),
        loaded = $form.data("loaded");

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


        $form.data("loaded", true);
    }
});