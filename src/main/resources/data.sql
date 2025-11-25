INSERT INTO project (id, name, description, category, start_date, end_date, priority, status, lead, tech_stack, repository, created_at)
VALUES
(1,
 'Platformă Observabilitate – AWS & Kubernetes',
 'Implementare completă de observabilitate cu metrici, loguri și auto-remediere.',
 'Observability & Monitoring',
 CURRENT_DATE - 10,
 CURRENT_DATE + 30,
 'CRITICAL',
 'IN_PROGRESS',
 'Alina Cioban',
 'Spring Boot, Docker, Prometheus, Grafana, AWS SDK',
 'https://github.com/example/observability-platform',
 CURRENT_DATE
),

(2,
 'Pipeline CI/CD Automatizat – GitHub Actions & ArgoCD',
 'Pipeline Enterprise pentru microservicii Java.',
 'DevOps',
 CURRENT_DATE - 20,
 CURRENT_DATE + 10,
 'HIGH',
 'ACTIVE',
 'Andrei Popescu',
 'GitHub Actions, ArgoCD, Docker',
 'https://github.com/example/cicd-automation',
 CURRENT_DATE
);

-- ============================
-- PROJECTS (DEMO DATA EXISTĂ!)
-- ============================
-- (Lasă proiectele tale exact cum sunt.)

-- =====================================
-- USERS (doar dacă nu ai deja în DB)
-- =====================================
INSERT INTO users (id, username, full_name, email, password, role)
VALUES
(1, 'alina', 'Alina Cioban', 'alina@example.com', 'password', 'ADMIN'),
(2, 'andrei', 'Andrei Popescu', 'andrei@example.com', 'password', 'USER')
ON CONFLICT (id) DO NOTHING;

-- =====================================
-- TASKS — DEMO DATA
-- =====================================
INSERT INTO task (id, project_id, title, description, priority, status, type,
                  estimated_hours, logged_hours, remaining_hours,
                  labels, components, sprint,
                  assignee_id, reporter_id,
                  due_date, created_at)
VALUES
-- TASK 1
(1, 1,
 'Instalare și configurare Prometheus',
 'Configurarea metricilor de bază pentru clusterul Kubernetes.',
 'HIGH', 'IN_PROGRESS', 'TASK',
 10, 4, 6,
 'monitoring', 'prometheus-server', 'Sprint 1',
 1, 1,
 CURRENT_DATE + 5, CURRENT_TIMESTAMP),

-- TASK 2
(2, 1,
 'Integrare Grafana Dashboards',
 'Crearea dashboardurilor pentru CPU, RAM, pod states.',
 'MEDIUM', 'TODO', 'TASK',
 6, 0, 6,
 'grafana,dashboard', 'grafana-core', 'Sprint 1',
 2, 1,
 CURRENT_DATE + 7, CURRENT_TIMESTAMP),

-- TASK 3
(3, 1,
 'Alerting cu Alertmanager',
 'Configurarea regulilor de alertare pentru aplicațiile critice.',
 'CRITICAL', 'TODO', 'BUG',
 4, 0, 4,
 'alerts', 'alertmanager', 'Sprint 2',
 1, 2,
 CURRENT_DATE + 3, CURRENT_TIMESTAMP),

-- TASK 4
(4, 2,
 'Creare pipeline build & test',
 'Pipeline complet GitHub Actions pentru microservicii.',
 'HIGH', 'IN_PROGRESS', 'STORY',
 12, 3, 9,
 'ci,testing', 'gha-runner', 'Sprint A',
 1, 2,
 CURRENT_DATE + 14, CURRENT_TIMESTAMP),

-- TASK 5
(5, 2,
 'Deploy automat în ArgoCD',
 'Configurarea aplicațiilor și sync-policy.',
 'CRITICAL', 'TODO', 'TASK',
 8, 0, 8,
 'argocd,deploy', 'argo-controller', 'Sprint B',
 2, 1,
 CURRENT_DATE + 10, CURRENT_TIMESTAMP),

-- TASK 6
(6, 2,
 'Fix bug: healthcheck pipeline',
 'Healthcheck-ul pentru staging nu funcționează.',
 'HIGH', 'TODO', 'BUG',
 2, 0, 2,
 'ci,bugfix', 'gha-runner', 'Sprint A',
 1, 1,
 CURRENT_DATE + 2, CURRENT_TIMESTAMP);

-- =====================================
-- OPTIONAL: DEMO COMMENTS
-- =====================================
INSERT INTO comment (id, task_id, author_id, content, created_at)
VALUES
(1, 1, 1, 'Am configurat Prometheus, urmează dashboards.', CURRENT_TIMESTAMP),
(2, 1, 2, 'Verific diseară metricile noi.', CURRENT_TIMESTAMP),
(3, 4, 1, 'Pipeline rulează, dar testele sunt lente.', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;
