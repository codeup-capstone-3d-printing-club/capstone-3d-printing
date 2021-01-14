// Necessary for camera/plane rotation

import * as THREE from './util/three.module.js';
import {STLLoader} from './util/STLLoader.js';
import * as os from './util/OrbitControls.js';
// import * as dat from './dat.gui/build/dat.gui';
// import * as light from "./dat.gui/build/dat.gui.module.js";

// const gui = new dat.GUI();
//controls

// let guiControls = new function(){
//     this.rotationX = 0.01;
//     this.rotationY = 0.01;
//     this.rotationZ = 0.01;
// }
// gui.add(guiControls,'rotationX',0,1);
// gui.add(guiControls,'rotationY',0,1);
// gui.add(guiControls,'rotationZ',0,1);
// const lightFolder = gui.addFolder('THREE.Light')
// lightFolder.addColor(data, 'color').onChange(() => {
//     light.color.setHex(Number(data.color.toString().replace('#', '0x')))
// });
// lightFolder.add(light, 'intensity', 0, 1, 0.01);
//
// const ambientLightFolder = gui.addFolder('THREE.AmbientLight')
// ambientLightFolder.open()
//
// const meshesFolder = gui.addFolder('Meshes')
// meshesFolder.add(data, 'mapsEnabled').onChange(() => {
//     material.forEach(m => {
//         if (data.mapsEnabled) {
//             m.map = texture
//         } else {
//             m.map = null;
//         }
//         m.needsUpdate = true;
//     })
// });

console.log(fileUrl)
let degree = Math.PI / 180;
let container = document.getElementById('renderer');
// Setup
let scene = new THREE.Scene();
let camera = new THREE.PerspectiveCamera(100, window.innerWidth / window.innerHeight, 0.01, 200);
let renderer = new THREE.WebGLRenderer({antialias: true});
renderer.setPixelRatio(window.devicePixelRatio);
renderer.setSize(window.innerWidth / 2, window.innerHeight / 2);
renderer.outputEncoding = THREE.sRGBEncoding;
renderer.shadowMap.enabled = true;
container.appendChild(renderer.domElement);

// Resize after viewport-size-change
window.addEventListener('resize', onWindowResize, false);

function onWindowResize() {

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize($(container).width(), $(container).height());
}

//template window resize function
// window.addEventListener("resize", function () {
//     let height = window.innerHeight;
//     let width = window.innerWidth;
//     renderer.setSize(width, height);
//     camera.aspect = width / height;
//     camera.updateProjectionMatrix();
// });

//background fog kinda sets the color of the object
scene.background = new THREE.Color(0x808080);
scene.fog = new THREE.Fog(0x111111, 0, 1500);
// Adding controls
let controls = new os.OrbitControls(camera, renderer.domElement);

// Ground (comment out line: "scene.add( plane );" if Ground is not needed...)
let plane = new THREE.Mesh(
    new THREE.PlaneBufferGeometry(150, 150),
    new THREE.MeshPhongMaterial({color: 0x999999, specular: 0x101010})
);
plane.rotation.x = degree;
plane.position.y = 0;
//does not raise and lower plane
plane.position.x = 0;
plane.receiveShadow = true;
scene.add(plane);


// ASCII file - STL Import
let loader = new STLLoader(); //loaders are for different files(ascii/binary), however you can't use them together bc it will render the file 2/3 times
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

loader.load(fileUrl, function (geometry) {
    let meshMaterial = new THREE.MeshLambertMaterial({color: 0xFFFFFF, specular: 0x111111, shininess: 200});
    if (geometry.hasColors) {
        meshMaterial = new THREE.MeshPhongMaterial({opacity: geometry.alpha, vertexColors: true});
    }
    let mesh = new THREE.Mesh(geometry, meshMaterial);
    //raise mesh to lift model off the plane so they don't intersect
    mesh.position.set(0, 0, 0);
    mesh.scale.set(0.35, 0.35, 0.35);
    mesh.castShadow = true;
    mesh.receiveShadow = true;
    scene.add(mesh);

});

// Camera positioning
camera.position.z = 25;
// camera.position.x = 0; doesn't allow for rotation
camera.position.y = -80;
camera.rotation.x = -45 / degree;

camera.position.setLength(5);
let cameraTarget = new THREE.Vector3(0,0,0);

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

// Draw scene
let render = function () {
    renderer.render(scene, camera);
    camera.lookAt(cameraTarget);
    // scene.rotation.x += guiControls.rotationX;
    // scene.rotation.y += guiControls.rotationY;
    // scene.rotation.z += guiControls.rotationZ;

};

// Run game loop (render,repeat)
let GameLoop = function () {
    requestAnimationFrame(GameLoop);

    render();
};

GameLoop();