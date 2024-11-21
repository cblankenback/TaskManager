-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema TaskManager
-- -----------------------------------------------------

-- -----fk_EMPLOYEETASK_TASK1`------------------------------------------------
-- Schema TaskManager
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `TaskManager` DEFAULT CHARACTER SET utf8 ;
USE `TaskManager` ;

-- -----------------------------------------------------
-- Table `TaskManager`.`DEPARTMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`department` (
  `DEPARTMENT_ID` INT NOT NULL AUTO_INCREMENT,
  `DEPARTMENT_NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`DEPARTMENT_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`AVAILABILITY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`availability` (
  `AVAILABILITY_ID` INT NOT NULL AUTO_INCREMENT,
  `AVAILABILITY_NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`AVAILABILITY_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`ROLE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`role` (
  `ROLE_ID` INT NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ROLE_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`EMPLOYEE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`employee` (
  `EMPLOYEE_ID` INT NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(45) NOT NULL unique,
  `FIRST_NAME` VARCHAR(45) NOT NULL,
  `LAST_NAME` VARCHAR(45) NOT NULL,
  `PASSWORD` VARCHAR(255) NOT NULL,
  `DEPARTMENT_ID` INT NOT NULL,
  `AVAILABILITY_ID` INT NOT NULL,
  `ROLE_ID` INT NOT NULL,
  PRIMARY KEY (`EMPLOYEE_ID`),
  INDEX `fk_EMPLOYEE_DEPARTMENT_idx` (`DEPARTMENT_ID` ASC) VISIBLE,
  INDEX `fk_EMPLOYEE_AVAILABILITY1_idx` (`AVAILABILITY_ID` ASC) VISIBLE,
  INDEX `fk_EMPLOYEE_ROLE1_idx` (`ROLE_ID` ASC) VISIBLE,
  CONSTRAINT `fk_EMPLOYEE_DEPARTMENT`
    FOREIGN KEY (`DEPARTMENT_ID`)
    REFERENCES `TaskManager`.`DEPARTMENT` (`DEPARTMENT_ID`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_EMPLOYEE_AVAILABILITY1`
    FOREIGN KEY (`AVAILABILITY_ID`)
    REFERENCES `TaskManager`.`AVAILABILITY` (`AVAILABILITY_ID`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_EMPLOYEE_ROLE1`
    FOREIGN KEY (`ROLE_ID`)
    REFERENCES `TaskManager`.`ROLE` (`ROLE_ID`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`PRIORITY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`priority` (
  `PRIORITY_ID` INT NOT NULL AUTO_INCREMENT,
  `TYPE` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`PRIORITY_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`TASK`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`task` (
  `TASK_ID` INT NOT NULL AUTO_INCREMENT,
  `TASK_NAME` VARCHAR(45) NOT NULL,
  `DESCRIPTION` VARCHAR(255) NOT NULL,
  `DEADLINE` DATETIME NOT NULL,
  `CREATION_DATE` DATETIME NOT NULL,
  `ARCHIVED` Boolean NOT NULL DEFAULT 0,
  `DEPENDENCY_TASK_ID` INT NULL,
  `CREATED_BY` INT NOT NULL,
  `PRIORITY_ID` INT NULL,
  `ADDRESS` VARCHAR(255),
  PRIMARY KEY (`TASK_ID`),
  INDEX `fk_TASK_TASK1_idx` (`DEPENDENCY_TASK_ID` ASC) VISIBLE,
  INDEX `fk_TASK_EMPLOYEE1_idx` (`CREATED_BY` ASC) VISIBLE,
  INDEX `fk_TASK_PRIORITY1_idx` (`PRIORITY_ID` ASC) VISIBLE,
  CONSTRAINT `fk_TASK_TASK1`
    FOREIGN KEY (`DEPENDENCY_TASK_ID`)
    REFERENCES `TaskManager`.`TASK` (`TASK_ID`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TASK_EMPLOYEE1`
    FOREIGN KEY (`CREATED_BY`)
    REFERENCES `TaskManager`.`EMPLOYEE` (`EMPLOYEE_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TASK_PRIORITY1`
    FOREIGN KEY (`PRIORITY_ID`)
    REFERENCES `TaskManager`.`PRIORITY` (`PRIORITY_ID`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`EMPLOYEETASK`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`employeetask` (
  `EMPLOYEETASK_ID` INT NOT NULL AUTO_INCREMENT,
  `ASSIGNED_DATE` DATETIME NOT NULL,
  `TASK_ID` INT NOT NULL,
  `EMPLOYEE_ID` INT NOT NULL,
  PRIMARY KEY (`EMPLOYEETASK_ID`),
  INDEX `fk_EMPLOYEETASK_TASK1_idx` (`TASK_ID` ASC) VISIBLE,
  INDEX `fk_EMPLOYEETASK_EMPLOYEE1_idx` (`EMPLOYEE_ID` ASC) VISIBLE,
  CONSTRAINT `fk_EMPLOYEETASK_TASK1`
	FOREIGN KEY (`TASK_ID`)
	REFERENCES `TaskManager`.`TASK` (`TASK_ID`)
	ON DELETE RESTRICT
	ON UPDATE NO ACTION,
  CONSTRAINT `fk_EMPLOYEETASK_EMPLOYEE1`
	FOREIGN KEY (`EMPLOYEE_ID`)
	REFERENCES `TaskManager`.`EMPLOYEE` (`EMPLOYEE_ID`)
	ON DELETE RESTRICT
	ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`COMMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`comment` (
  `COMMENT_ID` INT NOT NULL AUTO_INCREMENT,
  `MESSAGE` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`COMMENT_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`STATUS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`status` (
  `STATUS_ID` INT NOT NULL AUTO_INCREMENT,
  `STATUS_NAME` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`STATUS_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`TASKUPDATE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`taskupdate` (
  `TASKUPDATE_ID` INT NOT NULL AUTO_INCREMENT,
  `UPDATE_DATE` DATETIME NOT NULL,
  `TASK_ID` INT NOT NULL,
  `COMMENT_ID` INT NULL,
  `STATUS_ID` INT NOT NULL,
  `EMPLOYEE_ID` INT NOT NULL,
  PRIMARY KEY (`TASKUPDATE_ID`),
  INDEX `fk_TASKUPDATE_TASK1_idx` (`TASK_ID` ASC) VISIBLE,
  INDEX `fk_TASKUPDATE_COMMENT1_idx` (`COMMENT_ID` ASC) VISIBLE,
  INDEX `fk_TASKUPDATE_STATUS1_idx` (`STATUS_ID` ASC) VISIBLE,
  INDEX `fk_TASKUPDATE_EMPLOYEE1_idx` (`EMPLOYEE_ID` ASC) VISIBLE,
  CONSTRAINT `fk_TASKUPDATE_TASK1`
    FOREIGN KEY (`TASK_ID`)
    REFERENCES `TaskManager`.`TASK` (`TASK_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TASKUPDATE_COMMENT1`
    FOREIGN KEY (`COMMENT_ID`)
    REFERENCES `TaskManager`.`COMMENT` (`COMMENT_ID`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TASKUPDATE_STATUS1`
    FOREIGN KEY (`STATUS_ID`)
    REFERENCES `TaskManager`.`STATUS` (`STATUS_ID`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TASKUPDATE_EMPLOYEE1`
    FOREIGN KEY (`EMPLOYEE_ID`)
    REFERENCES `TaskManager`.`EMPLOYEE` (`EMPLOYEE_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`NOTIFICATION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`notification` (
  `NOTIFICATION_ID` INT NOT NULL AUTO_INCREMENT,
  `MESSAGE` VARCHAR(500) NOT NULL,
  `TASK_ID` INT NULL,
  PRIMARY KEY (`NOTIFICATION_ID`),
  INDEX `fk_NOTIFICATION_TASK1_idx` (`TASK_ID` ASC) VISIBLE,
  CONSTRAINT `fk_NOTIFICATION_TASK1`
    FOREIGN KEY (`TASK_ID`)
    REFERENCES `TaskManager`.`TASK` (`TASK_ID`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TaskManager`.`EMPLOYEENOTIFICATION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TaskManager`.`employeenotification` (
  `EMPLOYEENOTIFICATION_ID` INT NOT NULL AUTO_INCREMENT,
  `ISREAD` Boolean NOT NULL,
  `RECEIVED` DATETIME NULL,
  `NOTIFICATION_ID` INT NOT NULL,
  `EMPLOYEE_ID` INT NOT NULL,
  PRIMARY KEY (`EMPLOYEENOTIFICATION_ID`),
  INDEX `fk_EMPLOYEENOTIFICATION_NOTIFICATION1_idx` (`NOTIFICATION_ID` ASC) VISIBLE,
  INDEX `fk_EMPLOYEENOTIFICATION_EMPLOYEE1_idx` (`EMPLOYEE_ID` ASC) VISIBLE,
  CONSTRAINT `fk_EMPLOYEENOTIFICATION_NOTIFICATION1`
    FOREIGN KEY (`NOTIFICATION_ID`)
    REFERENCES `TaskManager`.`NOTIFICATION` (`NOTIFICATION_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_EMPLOYEENOTIFICATION_EMPLOYEE1`
    FOREIGN KEY (`EMPLOYEE_ID`)
    REFERENCES `TaskManager`.`EMPLOYEE` (`EMPLOYEE_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;






-- Use the TaskManager schema
USE `TaskManager`;

-- Populate the DEPARTMENT table
INSERT INTO `DEPARTMENT` (`DEPARTMENT_NAME`) VALUES
('Engineering'),   -- DEPARTMENT_ID = 1
('Marketing'),     -- DEPARTMENT_ID = 2
('Human Resources'), -- DEPARTMENT_ID = 3
('Sales'),         -- DEPARTMENT_ID = 4
('Finance');       -- DEPARTMENT_ID = 5

-- Populate the ROLE table
INSERT INTO `ROLE` (`ROLE_NAME`) VALUES
('Manager'),  -- ROLE_ID = 1
('Worker');   -- ROLE_ID = 2

-- Populate the AVAILABILITY table
INSERT INTO `AVAILABILITY` (`AVAILABILITY_NAME`) VALUES
('Available'),  -- AVAILABILITY_ID = 1
('Busy'),       -- AVAILABILITY_ID = 2
('On Leave');   -- AVAILABILITY_ID = 3

-- Populate the STATUS table
INSERT INTO `STATUS` (`STATUS_NAME`) VALUES
('Started'),      -- STATUS_ID = 1
('In Progress'),  -- STATUS_ID = 2
('Completed');    -- STATUS_ID = 3

-- Populate the PRIORITY table
INSERT INTO `PRIORITY` (`PRIORITY_ID`, `TYPE`) VALUES
(1, 'Low'),
(2, 'Medium'),
(3, 'High');

-- Populate the EMPLOYEE table
INSERT INTO `EMPLOYEE` (`USERNAME`, `FIRST_NAME`, `LAST_NAME`, `PASSWORD`, `DEPARTMENT_ID`, `AVAILABILITY_ID`, `ROLE_ID`) VALUES
('JohnDoe1', 'John', 'Doe', '$2b$12$e0NRj6w8O8FZs1pV5qSOUu77IXzJ6Oe0zDq6N3rWq3BvK0Fdfb3mi', 1, 1, 1),
('Jane' ,'Jane', 'Smith', '$2b$12$u1V7F9g3H0J8T1pW2xYTAu44JYzK7Pj9fZq2O4tXr2DfJ9U3pG8Kl', 2, 1, 2),
( 'Alice','Alice', 'Johnson', '$2b$12$h3K5L7m9N1P3R5tV7zA9Bu88LXzN9Qk6sYp0R1wZs4DhL5V6rH1Nj', 3, 2, 2),
( 'Bob','Bob', 'Brown', '$2b$12$n4Q6S8u0V2W4X6yZ8bC0Cv99NYzO0Rl7tZq1S2xXt5EgM0W1vJ2Op', 1, 1, 1),
('Charlie' ,'Charlie', 'Davis', '$2b$12$k5L7N9p1R3T5V7xA9D0Eu00OZzP1Sn8uYq2T3wXu6FhN1Y2sK3Qr', 4, 3, 2);

-- Verify DEPARTMENT entries
SELECT * FROM `department`;

-- Verify ROLE entries
SELECT * FROM `role`;

-- Verify AVAILABILITY entries
SELECT * FROM `availability`;

-- Verify STATUS entries
SELECT * FROM `status`;

-- Verify PRIORITY entries
SELECT * FROM `priority`;

-- Verify EMPLOYEE entries
SELECT * FROM `employee`;


