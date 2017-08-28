$JQry(function () {
    var $form = $JQry(".forum-thread form"),
        loaded = $form.data("loaded");

    if (!loaded) {
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
                    console.log(data); // FIXME
                    // TODO

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
