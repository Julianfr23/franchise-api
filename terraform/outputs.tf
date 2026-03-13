output "ecr_repository_url" {
  description = "ECR repository URL to push Docker images"
  value       = aws_ecr_repository.franchise_api.repository_url
}

output "ecs_cluster_name" {
  description = "ECS Cluster name"
  value       = aws_ecs_cluster.main.name
}

output "docdb_endpoint" {
  description = "DocumentDB cluster endpoint"
  value       = aws_docdb_cluster.main.endpoint
}
