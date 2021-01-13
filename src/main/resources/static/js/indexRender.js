
import * as THREE from './util/three.module.js';
import {STLLoader} from './util/STLLoader.js';

let degree = Math.PI / 180;



//background fog kinda sets the color of the object
scene.background = new THREE.Color(0xadabab);
scene.fog = new THREE.Fog(0x111111, 0, 500);
// Ground (comment out line: "scene.add( plane );" if Ground is not needed...)
let plane = new THREE.Mesh(
    new THREE.PlaneBufferGeometry(1000, 1000),
    new THREE.MeshPhongMaterial({color: 0x999999, specular: 0x101010})
);
plane.rotation.x = -90 * degree;
plane.position.y = -50;
plane.position.x = 0;
plane.receiveShadow = true;
scene.add(plane);


// Camera positioning
camera.position.z = 100;
camera.position.y = 50;
camera.rotation.x = degree;
let cameraTarget = new THREE.Vector3(0, -0.25, 0);

// Ambient light (necessary for Phong/Lambert-materials, not for Basic)
let ambientLight = new THREE.AmbientLight(0xffffff, 0.01);
scene.add(ambientLight);

scene.add(new THREE.HemisphereLight(0x443333, 0x111122));

function addShadowedLight(x, y, z, color, intensity) {
    const directionalLight = new THREE.DirectionalLight(color, intensity);
    directionalLight.position.set(x, y, z);
    scene.add(directionalLight);
    directionalLight.castShadow = true;
    const d = 1;
    directionalLight.shadow.camera.left = -d;
    directionalLight.shadow.camera.right = d;
    directionalLight.shadow.camera.top = d;
    directionalLight.shadow.camera.bottom = -d;
    directionalLight.shadow.camera.near = 1;
    directionalLight.shadow.camera.far = 4;
    directionalLight.shadow.bias = -0.002;
}

addShadowedLight(1, 1, 1, 0xffffff, 1.35);
addShadowedLight(0.5, 1, -1, 0xffaa00, 1);
let animate = function () {
    requestAnimationFrame(animate);
};
function onWindowResize(i, container) {

    camera[i].aspect = window.innerWidth / window.innerHeight;
    camera[i].updateProjectionMatrix();

    renderer[i].setSize($(container).width(), $(container).height());
}
let render = [];
let scene = [];
let camera = [];
let renderer = [];
let loader = [];
let mesh = [];
let i;
for(i = 0;i < 10;i++) {
    let idString = "renderIndex" + i.toString();
    let fileUrl = $('#renderIndex' + i.toString()).data('original-title');
    console.log(fileUrl);
    if(!document.getElementById(idString)){
        continue;
    }
    let container = document.getElementById(idString)
// Setup
    scene[i] = new THREE.Scene();
    camera[i] = new THREE.PerspectiveCamera(100, window.innerWidth / window.innerHeight, 0.1, 1000);
    renderer[i] = new THREE.WebGLRenderer();
    renderer[i].setPixelRatio(window.devicePixelRatio);
    renderer[i].setSize(window.innerWidth / 2, window.innerHeight / 2);
    renderer[i].outputEncoding = THREE.sRGBEncoding;
    renderer[i].shadowMap.enabled = true;
    renderer[i].setSize($(container).width(), $(container).height());
    container.appendChild(renderer[i].domElement);


// Resize after viewport-size-change
    window.addEventListener('resize',function(){onWindowResize(i,container)} , false);


// ASCII file - STL Import
    loader[i] = new STLLoader(); //loaders are for different files(ascii/binary), however you can't use them together bc it will render the file 2/3 times
// loader.load(fileUrl, function (geometry) {
//     let material = new THREE.MeshLambertMaterial({color: 0xFFFFFF, specular: 0x111111, shininess: 200});
//     let mesh = new THREE.Mesh(geometry, material);
//     mesh.position.set(0, 0, 0);
//     scene.add(mesh);
// });

// Binary files - STL Import
// loader.load(fileUrl, function (geometry) {
//     let material = new THREE.MeshLambertMaterial({color: 0xFFFFFF, specular: 0x111111, shininess: 200});
//     let mesh = new THREE.Mesh(geometry, material);
//     mesh.position.set(0, 20, 0);
//     scene.add(mesh);
// });

// Colored binary STL
    loader[i].load(fileUrl, function (geometry) {
        let meshMaterial = new THREE.MeshLambertMaterial({color: 0xFFFFFF});
        if (geometry.hasColors) {
            meshMaterial = new THREE.MeshPhongMaterial({opacity: geometry.alpha, vertexColors: true});
        }
        mesh[i] = new THREE.Mesh(geometry, meshMaterial);
        mesh[i].position.set(0, 0, 0);
        mesh[i].scale.set(0.3, 0.3, 0.3);
        mesh[i].castShadow = true;
        mesh[i].receiveShadow = true;
        scene[i].add(mesh);
    });

// Draw scene
        render[i]  = function () {
        renderer[i].render(scene, camera);
        camera[i].lookAt(cameraTarget);
    };

// Run game loop (render,repeat)
    render[i]();
    animate();
console.log( idString + " has rendered");
}
