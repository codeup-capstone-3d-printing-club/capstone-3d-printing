DROP DATABASE IF EXISTS printing_club_db;
CREATE DATABASE printing_club_db;

USE printing_club_db;

INSERT INTO users (avatar_url, email, first_name, is_admin, is_verified, joined_at, last_name, password, username)
VALUES ('none', 'test@test.com', 'tester', true, true, NOW(), 'last name test', '$2a$10$VRgC7cdxs6YbWfgjbzuNbumKC6uoU57vXUlOXafN70emLzT9MvFPO', 'testing'),
       ('none', 'john@john.com', 'john', true, true, NOW(), 'mcnay', '$2a$10$VRgC7cdxs6YbWfgjbzuNbumKC6uoU57vXUlOXafN70emLzT9MvFPO', 'johnmcnay'),
       ('none', 'messenger@messenger.com', 'messenger', true, true, NOW(), 'jones', '$2a$10$VRgC7cdxs6YbWfgjbzuNbumKC6uoU57vXUlOXafN70emLzT9MvFPO', 'messenger');


INSERT INTO messages (message, sent_at, recipient_id, sender_id, unread)
VALUES ('this is a test', NOW(), 1, 2, true),
       ('replying to the test', NOW(), 2, 1, true),
       ('second test', NOW(), 1, 2, true),
       ('second reply', NOW(), 2, 1, true);

INSERT INTO files (created_at, description, title, file_url, img_url, is_private, updated_at, owner_id)
VALUES (NOW(), 'this is a description for #1', 'file #1', 'none', 'none', false, NOW(), 1),
       (NOW(), 'this is a description for #2', 'file #2', 'none', 'none', false, NOW(), 1),
       (NOW(), 'this is a description for #3', 'file #3', 'none', 'none', false, NOW(), 2),
       (NOW(), 'this is a description for #4', 'file #4', 'none', 'none', false, NOW(), 2),
       (NOW(), 'this is a description for #5', 'file #5', 'none', 'none', false, NOW(), 3),
       (NOW(), 'this is a description for #6', 'file #6', 'none', 'none', false, NOW(), 3);