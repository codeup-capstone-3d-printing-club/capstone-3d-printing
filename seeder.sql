USE printing_club_db;

TRUNCATE users;
TRUNCATE messages;

INSERT INTO users (avatar_url, email, first_name, is_admin, is_verified, joined_at, last_name, password, username)
VALUES ('none','test@test.com', 'tester', true, true, NOW(), 'last name test', 'codeup', 'testing'),
       ('none','john@john.com', 'john', true, true, NOW(), 'mcnay', 'codeup', 'johnmcnay');


INSERT INTO messages (message, sent_at, recipient_id, sender_id)
VALUES ('this is a test', NOW(), 1, 2),
       ('replying to the test', NOW(), 2, 1);

