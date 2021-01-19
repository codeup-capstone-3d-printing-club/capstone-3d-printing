window.addEventListener('DOMContentLoaded', function () {
    const client = filestack.init(FILESTACK_API_KEY);
    const options = {
        maxFiles: 20,
        uploadInBackground: false,
        accept: [".stl"],
        onUploadDone: uploadFile
    };
    const picker = client.picker(options);
    const openBtn = document.getElementById('open');
    openBtn.addEventListener('click', () => picker.open());

    function uploadFile(result) {
        const fileData = result.filesUploaded[0];
        console.log(fileData);
        console.log(fileData.url);
        console.log(fileData.mimeType);
        $('#pickedFile').val(fileData.url);
        $('#open').text(fileData.filename);
        $('#uploadMessage').text('Upload Succeed!');
    }
});