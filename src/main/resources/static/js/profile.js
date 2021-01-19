function deleteFile(fileId) {
    $('#delete-btn').prop("disabled", true);

    $.ajax({
        url: "/files/" + fileId + "/delete?" + $("#delete-form").serialize(),
        type: "POST",
        headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
        success: function () {
            let $num = $('#uploaded-files');
            $num.text(parseInt($num.text()) - 1);
            $('#file-' + fileId).fadeOut(1500);
            $('#delete-btn').prop("disabled", false);
        }
    });
}
