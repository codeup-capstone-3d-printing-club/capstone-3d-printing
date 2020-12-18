DROP DATABASE IF EXISTS capstone_db;
CREATE DATABASE capstone_db;

USE capstone_db;

## need to change the database name above

INSERT INTO users (avatar_url, email, first_name, is_admin, is_verified, joined_at, last_name, password, username, is_flagged, is_active)

VALUES ('none', 'no-reply@squarecubed.xyz', 'admin', true, true, NOW(), 'admin', '$2a$10$VRgC7cdxs6YbWfgjbzuNbumKC6uoU57vXUlOXafN70emLzT9MvFPO', 'admin', false, true),
       ('none', 'john@john.com', 'john', false, true, NOW(), 'mcnay', '$2a$10$VRgC7cdxs6YbWfgjbzuNbumKC6uoU57vXUlOXafN70emLzT9MvFPO', 'johnmcnay',false, true),
       ('none', 'messenger@messenger.com', 'messenger', false, true, NOW(), 'jones', '$2a$10$VRgC7cdxs6YbWfgjbzuNbumKC6uoU57vXUlOXafN70emLzT9MvFPO', 'messenger',false, true);


INSERT INTO messages (message, sent_at, recipient_id, sender_id, unread)
VALUES ('this is a test', NOW(), 1, 2, true),
       ('replying to the test', NOW(), 2, 1, true),
       ('second test', NOW(), 1, 2, true),
       ('second reply', NOW(), 2, 1, true);

INSERT INTO files (created_at, description, title, file_url, img_url, is_private, updated_at, owner_id, is_flagged)
VALUES (NOW(), 'this is a description for #1', 'file #1', 'none', 'none', false, NOW(), 1, false),
       (NOW(), 'this is a description for #2', 'file #2', 'none', 'none', false, NOW(), 1, false),
       (NOW(), 'this is a description for #3', 'file #3', 'none', 'none', false, NOW(), 2, false),
       (NOW(), 'this is a description for #4', 'file #4', 'none', 'none', false, NOW(), 2, false),
       (NOW(), 'this is a description for #5', 'file #5', 'none', 'none', false, NOW(), 3, false),
       (NOW(), 'this is a description for #6', 'file #6', 'none', 'none', false, NOW(), 3, false);

INSERT INTO categories(category)
VALUES ('Art'),
       ('Airplane'),
       ('Anatomy'),
       ('Animal'),
       ('Architechture'),
       ('Car'),
       ('Character'),
       ('Food & Drink'),
       ('Furnishings'),
       ('Industrial'),
       ('Interior Design'),
       ('Nature'),
       ('Man'),
       ('Office'),
       ('People'),
       ('Plants'),
       ('Robot'),
       ('Technology'),
       ('Trees'),
       ('Vehicles'),
       ('Woman');

INSERT INTO file_category(file_id, category_id)
VALUES (1, 1),

       (2, 2),
       (3, 3),
       (3, 1),
       (4, 2),
       (5, 3),
       (6, 4);

INSERT INTO settings(description)
VALUES ('Followed user posts a file'),
       ('User follows you'),
       ('A file you posted receives a comment');
