window.addEventListener('DOMContentLoaded', function () {
    const client = filestack.init(FILESTACK_API_KEY);
    const options = {
        maxFiles: 1,
        uploadInBackground: false,
        onUploadDone: uploadFile,
        transformations: {
            crop: true,
            rotate: true
        }
    };
    const picker = client.picker(options);
    const openBtn = document.getElementById('open');
    openBtn.addEventListener('click',() => picker.open());

    function uploadFile(result) {
        const fileData = result.filesUploaded[0];
        console.log(fileData);
        console.log(fileData.url);
        console.log(fileData.mimeType);
        $('#pickedFile').val(fileData.url);
        $('#submitAvatar').submit();
    }

});