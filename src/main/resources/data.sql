INSERT INTO public.ear_user (id, name, password, removed, role, username, avatar_id) VALUES (69, 'Admin', '$2a$10$dWyTWEanyG6WANrnqCUW9.WuFXoWb5Gvlmzrse9QALJcnmsQuDQaO', false, 'ADMIN', 'admin', null);
INSERT INTO public.ear_user (id, name, password, removed, role, username, avatar_id) VALUES (64, 'HAASD', '$2a$10$viDpd5pi45LPcDs/44kwbOlOIYzCIHWcJ0j0XSyqK9nOM4rqIViHG', false, 'USER', 'gerzitomG', null);
INSERT INTO public.ear_user (id, name, password, removed, role, username, avatar_id) VALUES (39, 'Tom', '$2a$10$D6B.J0HnbxPdnYNReqSkjeRW32QXzBosRlnESOeqV5G2qtP4H.raq', false, 'PROJECT_MANAGER', 'gerzitom', null);
INSERT INTO public.ear_user (id, name, password, removed, role, username, avatar_id) VALUES (35, 'Martin Hubal', '$2a$10$Yy4V5TiDP81x7NgDxJ6VM.iQiyd56rUjPAk0ahBCZ9wehBTFcdzQi', true, 'USER', 'hubalmara', null);

INSERT INTO public.project (project_id, deadline, name, state) VALUES (1, null, 'Spotify PWA app', 'IN_PROGRESS');
INSERT INTO public.project (project_id, deadline, name, state) VALUES (2, null, 'Special Tom project', 'IN_PROGRESS');
INSERT INTO public.project (project_id, deadline, name, state) VALUES (4, null, 'New project', 'IN_PROGRESS');
INSERT INTO public.project (project_id, deadline, name, state) VALUES (3, null, 'New project with user', 'IN_PROGRESS');
INSERT INTO public.project (project_id, deadline, name, state) VALUES (12, '2021-01-03', 'EAR project', 'IN_PROGRESS');

INSERT INTO public.project_user (id, added, project_id, user_id) VALUES (67, '2020-12-29 05:11:43.641199', 1, 39);
INSERT INTO public.project_user (id, added, project_id, user_id) VALUES (68, '2020-12-29 06:45:11.608318', 1, 35);
INSERT INTO public.project_user (id, added, project_id, user_id) VALUES (81, '2021-01-01 15:47:45.714463', 3, 39);
INSERT INTO public.project_user (id, added, project_id, user_id) VALUES (91, '2021-01-01 23:24:18.616518', 3, 64);
INSERT INTO public.project_user (id, added, project_id, user_id) VALUES (98, '2021-01-02 21:35:36.318469', 12, 39);
INSERT INTO public.project_user (id, added, project_id, user_id) VALUES (99, '2021-01-02 21:35:43.031799', 12, 35);

INSERT INTO public.sprint (id, closed, deadline, description, name, project_id) VALUES (127, false, null, 'Specialized for creating services', 'Services sprint', 12);

INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (41, '2020-12-30', 'Task desc', 'Next task for project', 'IN_PROGRESS', null, 1, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (40, '2020-12-31', 'Task desc', 'New task for project', 'DONE', null, 1, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (82, null, '', 'New task', 'IN_PROGRESS', null, 3, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (85, null, '', 'Next task', 'DONE', null, 3, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (100, '2021-01-05', 'Create JPA models for whole project', 'JPA models', 'IN_PROGRESS', null, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (110, null, '', 'Services', 'IN_PROGRESS', null, 12, null, 127);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (102, null, '', 'Project model', 'IN_PROGRESS', 100, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (103, null, '', 'Task model', 'IN_PROGRESS', 100, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (104, null, '', 'User model', 'IN_PROGRESS', 100, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (105, null, '', 'DAOs', 'IN_PROGRESS', null, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (107, null, '', 'Project DAO', 'IN_PROGRESS', 105, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (108, null, '', 'Task DAO', 'IN_PROGRESS', 105, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (109, null, '', 'User DAO', 'IN_PROGRESS', 105, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (120, null, '', 'Task User relation', 'IN_PROGRESS', 103, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (121, null, '', 'Project User relation', 'IN_PROGRESS', 102, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (122, '2021-01-04', '', 'Task service', 'IN_PROGRESS', 110, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (123, '2021-01-05', '', 'Project services', 'IN_PROGRESS', 110, 12, null, null);
INSERT INTO public.task (id, deadline, description, name, state, parent_task_id, project_id, responsible_user_id, sprint_id) VALUES (125, '2021-01-12', '', 'Some done task', 'DONE', null, 12, null, null);

INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (74, null, 41, 39);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (80, null, 41, 35);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (83, null, 82, 39);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (86, null, 85, 39);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (89, null, 82, 35);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (101, null, 100, 39);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (106, null, 105, 35);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (111, null, 110, 39);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (124, null, 123, 35);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (126, null, 125, 35);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (129, null, 107, 39);
INSERT INTO public.task_user (id, created, task_id, user_id) VALUES (132, null, 122, 39);

INSERT INTO public.tracked_time (id, description, time_end, time_start, task_id, user_id) VALUES (112, null, '2021-01-02 22:13:31.000000', '2021-01-02 22:04:48.000000', 110, 39);
INSERT INTO public.tracked_time (id, description, time_end, time_start, task_id, user_id) VALUES (113, null, '2021-01-02 22:33:51.000000', '2021-01-02 22:32:26.000000', 110, 39);
INSERT INTO public.tracked_time (id, description, time_end, time_start, task_id, user_id) VALUES (114, null, '2021-01-02 22:35:13.000000', '2021-01-02 22:33:58.000000', 110, 39);
INSERT INTO public.tracked_time (id, description, time_end, time_start, task_id, user_id) VALUES (115, null, '2021-01-02 22:50:14.000000', '2021-01-02 22:39:19.000000', 110, 39);
INSERT INTO public.tracked_time (id, description, time_end, time_start, task_id, user_id) VALUES (116, null, '2020-12-22 01:35:58.000000', '2021-01-02 22:51:33.000000', 110, 39);
INSERT INTO public.tracked_time (id, description, time_end, time_start, task_id, user_id) VALUES (117, null, '2020-12-22 01:35:58.000000', '2021-01-02 23:21:33.000000', 105, 39);
INSERT INTO public.tracked_time (id, description, time_end, time_start, task_id, user_id) VALUES (118, null, '2020-12-22 01:35:58.000000', '2021-01-02 23:23:10.000000', 100, 39);
INSERT INTO public.tracked_time (id, description, time_end, time_start, task_id, user_id) VALUES (119, null, '2021-01-02 23:58:47.000000', '2021-01-02 23:35:01.000000', 105, 39);
INSERT INTO public.tracked_time (id, description, time_end, time_start, task_id, user_id) VALUES (128, null, '2021-01-03 16:25:14.000000', '2021-01-03 16:24:03.000000', 107, 39);


INSERT INTO public.comment (id, created, text, task_id, user_id) VALUES (60, '2020-11-27 01:29:58.000000', 'HAHAH', 41, 35);
INSERT INTO public.comment (id, created, text, task_id, user_id) VALUES (63, '2020-11-28 01:39:58.000000', 'HAHAH', 41, 35);
INSERT INTO public.comment (id, created, text, task_id, user_id) VALUES (62, '2020-11-26 01:39:58.000000', 'HAHAH', 41, 39);
INSERT INTO public.comment (id, created, text, task_id, user_id) VALUES (61, '2020-11-26 01:29:58.000000', 'HAHAH', 41, 39);
INSERT INTO public.comment (id, created, text, task_id, user_id) VALUES (66, '2020-12-29 04:31:57.000000', 'Hey, this could be kinda good task!', 41, 39);
INSERT INTO public.comment (id, created, text, task_id, user_id) VALUES (90, '2021-01-01 11:23:40.000000', 'HAHA', 82, 39);
INSERT INTO public.comment (id, created, text, task_id, user_id) VALUES (130, '2021-01-03 04:34:10.000000', 'HAHA', 107, 39);
INSERT INTO public.comment (id, created, text, task_id, user_id) VALUES (131, '2021-01-03 04:34:48.000000', 'Hey, there should be relation for  project and user.', 107, 39);