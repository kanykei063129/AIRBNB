insert into users (id, full_name, email, password, image, role)
values (1, 'Jonne Jones', 'bones@gmail.com', '$2a$12$5/Bok1UzVWfjtCohyblPruO9GWIugj3aC.nKXGfnORG3JtapTZutG', 'photo with belt', 'USER'),
       (2, 'Alister Overeem', 'alister@gmail.com', '$2a$12$5/Bok1UzVWfjtCohyblPruO9GWIugj3aC.nKXGfnORG3JtapTZutG', 'photo with kick', 'USER'),
       (3, 'Kyle Snyder', 'snyder@gmail.com', '$2a$12$5/Bok1UzVWfjtCohyblPruO9GWIugj3aC.nKXGfnORG3JtapTZutG','photo wrestling boots', 'USER'),
       (4, 'Kyle Dike', 'dike@gmail.com', '$2a$12$5/Bok1UzVWfjtCohyblPruO9GWIugj3aC.nKXGfnORG3JtapTZutG', 'photo 74', 'USER'),
       (5, 'David Taylor', 'admin@gmail.com', '$2a$12$0fNQj3htdAad9Y9BXc.Axep6wxi5RHskU0SbzjJCHHInjmetLiM9y','photo 86', 'USER');

insert into announcements
(id, house_type, price, region, province, address, description, status, title, max_guests, user_id)
values (1, 'HOUSE', 23, 'CHUI', 'Yssyk-Ata', '1-street 3', 'for sanatory', 'MODERATION', 'house', 3, 1),
       (2, 'HOUSE', 24, 'BATKEN', 'batken', '2-street 4', 'for Relax in the apricot garden', 'MODERATION', 'house', 5,
        2),
       (3, 'APARTMENT', 41, 'BISHKEK', 'ALAMIDIN-1', '3-street 4', 'for long time ', 'MODERATION', 'apartment', 2,
        3),
       (4, 'HOUSE', 30, 'ISSYK_KUL', 'Bosteri', '4-street 5', 'only for summer season', 'MODERATION', 'house', 3,
        4),
       (5, 'HOUSE', 20, 'NARYN', 'At-Bashy', '5-street 6', 'Tash-Rabat visit', 'MODERATION', 'house', 5, 5);

insert into announcement_images (announcement_id, images)
values (1, 'photo 1'),
       (1, 'photo 2'),
       (2, 'photo 1'),
       (2, 'photo 2'),
       (3, 'photo 1'),
       (3, 'photo 2'),
       (4, 'photo 1'),
       (4, 'photo 2'),
       (5, 'photo 1'),
       (5, 'photo 2');

insert into bookings(id, check_in, check_out, date, user_id, announcement_id)
values (1, '2023-08-14T10:30:00+00:00', '2023-08-15T10:30:00+00:00','2023-08-14T10:30:00+00:00', 1, 2),
       (2, '2023-09-14T10:30:00+00:00', '2023-09-17T10:30:00+00:00','2023-08-14T10:30:00+00:00', 2, 3),
       (3, '2023-09-14T10:30:00+00:00', '2023-09-17T10:30:00+00:00','2023-08-14T10:30:00+00:00', 3, 4),
       (4, '2023-10-14T10:30:00+00:00', '2023-10-17T10:30:00+00:00','2023-08-14T10:30:00+00:00', 4, 5),
       (5, '2023-10-14T10:30:00+00:00', '2023-10-17T10:30:00+00:00','2023-08-14T10:30:00+00:00', 5, 1);

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