INSERT INTO projects (id, name, description, created_at) VALUES
  (1, 'Website Redesign', 'Refresh the corporate website and improve UX.', CURRENT_TIMESTAMP);

INSERT INTO tasks (id, project_id, title, description, status, priority, assignee_id, due_date, created_at, updated_at) VALUES
  (1, 1, 'Gather requirements', 'Meet stakeholders and collect requirements.', 'TODO', 'HIGH', NULL, DATEADD('DAY', 7, CURRENT_DATE), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 1, 'Create wireframes', 'Design responsive wireframes for main pages.', 'IN_PROGRESS', 'MEDIUM', NULL, DATEADD('DAY', 14, CURRENT_DATE), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 1, 'Implement frontend', 'Build layouts with Bootstrap and Thymeleaf.', 'TODO', 'CRITICAL', NULL, DATEADD('DAY', 21, CURRENT_DATE), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 1, 'Set up CI/CD', 'Configure GitHub Actions for automated deploys.', 'TODO', 'HIGH', NULL, DATEADD('DAY', 28, CURRENT_DATE), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (5, 1, 'Write documentation', 'Document project architecture and processes.', 'TODO', 'LOW', NULL, DATEADD('DAY', 30, CURRENT_DATE), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (6, 1, 'QA testing', 'Run regression suite before launch.', 'IN_PROGRESS', 'HIGH', NULL, DATEADD('DAY', 25, CURRENT_DATE), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
