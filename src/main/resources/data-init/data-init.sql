insert into users (id, full_name, email, password, image, role)
values (1, 'Jonne Jones', 'bones@gmail.com', '$2a$12$KfOMw3D17qamXwGRkYcAbOjSzXmQLe8WYfPFJ4K/oNZskIYAWBo9q',
        'photo with belt', 'USER'),
       (2, 'Alister Overeem', 'alister@gmail.com', '$2a$12$KfOMw3D17qamXwGRkYcAbOjSzXmQLe8WYfPFJ4K/oNZskIYAWBo9q',
        'photo with kick', 'USER'),
       (3, 'Kyle Snyder', 'snyder@gmail.com', '$2a$12$KfOMw3D17qamXwGRkYcAbOjSzXmQLe8WYfPFJ4K/oNZskIYAWBo9q',
        'photo wrestling boots', 'USER'),
       (4, 'Kyle Dike', 'dike@gmail.com', '$2a$12$KfOMw3D17qamXwGRkYcAbOjSzXmQLe8WYfPFJ4K/oNZskIYAWBo9q', 'photo 74',
        'USER'),
       (5, 'David Taylor', 'admin@gmail.com', '$2a$12$ZDscdsNcu.jtmBFg.DAXo.fC/DAM12l.Z0Kz0nUw9ICb9GUriLXJy',
        'photo 86', 'ADMIN'),
       (6, 'Rus', 'rusi.studio.kgz@gmail.com', '$2a$12$99Q6ccCQVhM.Rv6lbXza4uGUTiJLZkzBBDiLHopPN1niFwMVPsw8W',
        'default image', 'USER');

INSERT INTO announcements
(id, house_type, price, region, province, address, description, status, title, max_guests, create_date,
 message_from_admin, user_id, position)
VALUES (1, 'HOUSE', 23, 'CHUI', 'Yssyk-Ata', '1-street 3', 'for sanatory', 'BOOKED', 'house', 3, NOW(), NULL, 1,
        'MODERATION'),
       (2, 'HOUSE', 24, 'BATKEN', 'batken', '2-street 4', 'for Relax in the apricot garden', 'NOT_BOOKED', 'house', 5,
        NOW(), NULL, 2, 'MODERATION'),
       (3, 'APARTMENT', 41, 'BISHKEK', 'ALAMIDIN-1', '3-street 4', 'for long time', 'BOOKED', 'apartment', 2, NOW(),
        NULL, 3, 'REJECT'),
       (4, 'HOUSE', 30, 'ISSYK_KUL', 'Bosteri', '4-street 5', 'only for summer season', 'NOT_BOOKED', 'house', 3, NOW(),
        NULL, 4, 'MODERATION'),
       (5, 'HOUSE', 20, 'NARYN', 'At-Bashy', '5-street 6', 'Tash-Rabat visit', 'BOOKED', 'house', 5, NOW(), NULL, 5,
        'ACCEPTED'),
       (6, 'APARTMENT', 28, 'BISHKEK', 'Alamedin', '12-street 13', 'Nearby amenities', 'BOOKED', 'apartment', 2, NOW(),
        NULL, 2, 'MODERATION'),
       (7, 'APARTMENT', 32, 'BISHKEK', 'Chui Prospect', '13-street 14', 'Central location', 'NOT_BOOKED', 'apartment',
        3, NOW(), NULL, 1, 'MODERATION'),
       (8, 'HOUSE', 18, 'JALAL_ABAD', 'Sary-Tash', '14-street 15', 'Rural retreat', 'BOOKED', 'house', 5, NOW(), NULL,
        3, 'MODERATION'),
       (9, 'APARTMENT', 20, 'OSH', 'Lenin', '15-street 16', 'Market access', 'NOT_BOOKED', 'apartment', 2, NOW(), NULL,
        2, 'ACCEPTED'),
       (10, 'APARTMENT', 20, 'OSH', 'Lenin', '15-street 16', 'Market access', 'NOT_BOOKED', 'apartment', 2, NOW(), NULL,
        6, 'REJECT'),
       (11, 'HOUSE', 22, 'TALAS', 'Ala-Bel', '16-street 17', 'Panoramic views', 'BOOKED', 'house', 4, NOW(), NULL, 6,
        'REJECT'),
       (12, 'APARTMENT', 25, 'BISHKEK', 'Bokonbaevo', '17-street 18', 'Cultural sites nearby', 'NOT_BOOKED',
        'apartment', 3, NOW(), NULL, 1, 'ACCEPTED');


insert into announcement_images (announcement_id, images)
values (1, 'https://www.myluxoria.com/storage/app/uploads/public/626/699/e07/626699e077a24792592533.jpg'),
       (1, 'https://www.myluxoria.com/storage/app/uploads/public/626/699/e07/626699e077a24792592533.jpg'),
       (2, 'https://www.myluxoria.com/storage/app/uploads/public/61c/d81/2d7/61cd812d7b38d287793939.jpg'),
       (2, 'https://www.myluxoria.com/storage/app/uploads/public/61c/d81/2d7/61cd812d7b38d287793939.jpg'),
       (3, 'https://www.myluxoria.com/storage/app/uploads/public/622/9c6/1c4/6229c61c4f3ff743328316.jpg'),
       (3, 'https://www.myluxoria.com/storage/app/uploads/public/622/9c6/1c4/6229c61c4f3ff743328316.jpg'),
       (4, 'https://www.myluxoria.com/storage/app/uploads/public/622/9c6/5b0/6229c65b00ba4507504618.jpg'),
       (4, 'https://www.myluxoria.com/storage/app/uploads/public/622/9c6/5b0/6229c65b00ba4507504618.jpg'),
       (5, 'https://www.myluxoria.com/storage/app/uploads/public/630/77d/9dd/63077d9dd7b37898060436.jpg'),
       (6, 'https://f.hubspotusercontent20.net/hubfs/4043727/pexels-max-vakhtbovych-7195322.jpg'),
       (7, 'https://nimvo.com/wp-content/uploads/2017/07/Glass-House-750x500.jpg'),
       (8, 'https://cdn.standardmedia.co.ke/images/friday/angjcpqletins5f92be8c7aa72.jpg'),
       (9, 'https://nimvo.com/wp-content/uploads/2017/07/Glass-House-750x500.jpg'),
       (10, 'https://cdn.standardmedia.co.ke/images/friday/angjcpqletins5f92be8c7aa72.jpg'),
       (11, 'https://f.hubspotusercontent20.net/hubfs/4043727/pexels-max-vakhtbovych-7195322.jpg'),
       (12, 'https://www.myluxoria.com/storage/app/uploads/public/630/77d/9dd/63077d9dd7b37898060436.jpg');

insert into bookings(id, check_in, check_out, date, user_id, announcement_id, position)
values (1, '2023-08-14T10:30:00+00:00', '2023-08-15T10:30:00+00:00', '2023-08-14T10:30:00+00:00', 1, 2, 1),
       (2, '2023-09-14T10:30:00+00:00', '2023-09-17T10:30:00+00:00', '2023-08-14T10:30:00+00:00', 2, 3, 1),
       (3, '2023-09-14T10:30:00+00:00', '2023-09-17T10:30:00+00:00', '2023-08-14T10:30:00+00:00', 3, 4, 2),
       (4, '2023-10-14T10:30:00+00:00', '2023-10-17T10:30:00+00:00', '2023-08-14T10:30:00+00:00', 4, 5, 2),
       (5, '2023-10-14T10:30:00+00:00', '2023-10-17T10:30:00+00:00', '2023-08-14T10:30:00+00:00', 5, 1, 2);

insert into favorites(id, user_id, announcement_id)
values (1, 1, 2),
       (2, 2, 1),
       (3, 3, 2),
       (4, 4, 3),
       (5, 5, 4);

insert into feedbacks(id, comment, dis_like_count, like_count, rating, create_date, user_id, announcement_id)
values (1, 'good for one 2 person', 0, 1, 4, '2023-7-14T10:30:00+00:00', 1, 3),
       (2, 'good for one 2 person', 0, 1, 4, '2023-7-14T10:30:00+00:00', 2, 4),
       (3, 'good for one 2 person', 0, 1, 4, '2023-7-14T10:30:00+00:00', 3, 5),
       (4, 'good for one 2 person', 0, 1, 4, '2023-7-14T10:30:00+00:00', 4, 1),
       (5, 'good for one 2 person', 0, 1, 4, '2023-7-14T10:30:00+00:00', 5, 2);

insert into feedback_images(feedback_id, images)
values (1, 'photo 1'),
       (1, 'photo 2'),
       (2, 'photo 2'),
       (2, 'photo 1'),
       (3, 'photo 2'),
       (3, 'photo 1'),
       (4, 'photo 2'),
       (4, 'photo 1'),
       (5, 'photo 2'),
       (5, 'photo 1');

insert into likes(id, is_liked, feedback_id, user_id)
values (1, false, 1, 4),
       (2, false, 2, 5),
       (3, false, 3, 1),
       (4, false, 4, 2),
       (5, false, 5, 3);