variable "aws_region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Project name prefix for all resources"
  type        = string
  default     = "franchise-api"
}

variable "db_username" {
  description = "DocumentDB master username"
  type        = string
  default     = "franchiseadmin"
  sensitive   = true
}

variable "db_password" {
  description = "DocumentDB master password"
  type        = string
  sensitive   = true
}
