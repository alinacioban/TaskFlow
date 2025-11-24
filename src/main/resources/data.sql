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