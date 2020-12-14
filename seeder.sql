USE printing_club_db;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE table messages;
TRUNCATE table users;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO users (avatar_url, email, first_name, is_admin, is_verified, joined_at, last_name, password, username)
VALUES ('none', 'test@test.com', 'tester', true, true, NOW(), 'last name test', 'codeup', 'testing'),
       ('none', 'john@john.com', 'john', true, true, NOW(), 'mcnay', 'codeup', 'johnmcnay'),
       ('none', 'messenger@messenger.com', 'messenger', true, true, NOW(), 'jones', 'codeup', 'messenger');


INSERT INTO messages (message, sent_at, recipient_id, sender_id, unread)
VALUES ('this is a test', NOW(), 1, 2, true),
       ('replying to the test', NOW(), 2, 1, true),
       ('second test', NOW(), 1, 2, true),
       ('second reply', NOW(), 2, 1, true);

