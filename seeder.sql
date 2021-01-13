DROP DATABASE IF EXISTS capstone_db;
CREATE DATABASE capstone_db;

USE capstone_db;

## need to change the database name above

INSERT INTO users (avatar_url, email, first_name, is_admin, is_verified, joined_at, last_name, password, username, is_flagged, is_active, is_private)

VALUES ('/image/placeholder-avatar.jpg', 'no-reply@squarecubed.xyz', 'admin', true, true, NOW(), 'admin', '$2a$10$VRgC7cdxs6YbWfgjbzuNbumKC6uoU57vXUlOXafN70emLzT9MvFPO', 'admin', false, true, false),
       ('/image/placeholder-avatar.jpg', 'john@john.com', 'john', false, true, NOW(), 'mcnay', '$2a$10$VRgC7cdxs6YbWfgjbzuNbumKC6uoU57vXUlOXafN70emLzT9MvFPO', 'johnmcnay',false, true, false),
       ('/image/placeholder-avatar.jpg', 'messenger@messenger.com', 'messenger', false, true, NOW(), 'jones', '$2a$10$VRgC7cdxs6YbWfgjbzuNbumKC6uoU57vXUlOXafN70emLzT9MvFPO', 'messenger',false, true, true);


INSERT INTO messages (message, sent_at, recipient_id, sender_id, unread)
VALUES ('this is a test', NOW(), 1, 2, true),
       ('replying to the test', NOW(), 2, 1, true),
       ('second test', NOW(), 1, 2, true),
       ('second reply', NOW(), 2, 1, true);

INSERT INTO files (created_at, description, title, file_url, is_private, updated_at, owner_id, is_flagged, average_rating)
VALUES (NOW(), 'heart ring signet', 'Heart Ring', 'https://cdn.filestackcontent.com/1BLLVcGQoyGcD48MZd0w', false, NOW(), 1, false, 0),
       (NOW(), 'table', 'table', 'https://cdn.filestackcontent.com/1BLLVcGQoyGcD48MZd0w', false, NOW(), 1, false, 0);


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
       (2, 2);

INSERT INTO settings(description)
VALUES ('Followed user posts a file'),
       ('User follows you'),
       ('A file you posted receives a comment');
