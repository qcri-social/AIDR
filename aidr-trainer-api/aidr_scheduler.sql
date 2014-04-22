-- phpMyAdmin SQL Dump
-- version 3.5.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 22, 2014 at 01:35 PM
-- Server version: 5.5.29
-- PHP Version: 5.4.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `aidr_scheduler`
--

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

CREATE TABLE `client` (
  `clientID` int(20) NOT NULL AUTO_INCREMENT,
  `aidrUserID` bigint(20) unsigned DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `hostURL` varchar(200) NOT NULL,
  `hostAPIKey` varchar(200) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `queueSize` int(10) unsigned NOT NULL DEFAULT '50',
  `aidrHostURL` varchar(100) DEFAULT NULL,
  `defaultTaskRunsPerTask` int(10) unsigned NOT NULL DEFAULT '3',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`clientID`),
  UNIQUE KEY `clientID_UNIQUE` (`clientID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `clientApp`
--

CREATE TABLE `clientApp` (
  `clientAppID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `clientID` bigint(20) unsigned NOT NULL,
  `crisisID` bigint(20) unsigned DEFAULT NULL,
  `nominalAttributeID` bigint(20) DEFAULT NULL,
  `platformAppID` bigint(20) unsigned NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(200) NOT NULL,
  `shortName` varchar(50) NOT NULL,
  `taskRunsPerTask` int(10) unsigned NOT NULL DEFAULT '1',
  `quorum` int(10) unsigned NOT NULL DEFAULT '1',
  `iconURL` varchar(200) DEFAULT 'http://i.imgur.com/lgZAWIc.png',
  `status` int(11) unsigned NOT NULL DEFAULT '1',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `appType` int(11) DEFAULT '1',
  PRIMARY KEY (`clientAppID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=83 ;

-- --------------------------------------------------------

--
-- Table structure for table `clientAppAnswer`
--

CREATE TABLE `clientAppAnswer` (
  `clientAppID` bigint(20) NOT NULL,
  `answer` text NOT NULL,
  `voteCutOff` int(10) unsigned NOT NULL DEFAULT '2',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`clientAppID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `clientAppEvent`
--

CREATE TABLE `clientAppEvent` (
  `clientAppEventID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `clientAppID` bigint(20) NOT NULL,
  `sequence` int(11) NOT NULL,
  `eventID` bigint(20) NOT NULL,
  `created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`clientAppEventID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

-- --------------------------------------------------------

--
-- Table structure for table `clientAppSource`
--

CREATE TABLE `clientAppSource` (
  `clientAppSourceID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `clientAppID` bigint(20) unsigned NOT NULL,
  `sourceURL` varchar(600) NOT NULL,
  `status` int(10) unsigned NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`clientAppSourceID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=73 ;

-- --------------------------------------------------------

--
-- Table structure for table `lookup`
--

CREATE TABLE `lookup` (
  `lookupID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `tableName` varchar(45) NOT NULL,
  `columnName` varchar(45) NOT NULL,
  `columnValue` int(11) NOT NULL,
  `description` varchar(200) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`lookupID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

-- --------------------------------------------------------

--
-- Table structure for table `reportTemplate`
--

CREATE TABLE `reportTemplate` (
  `reportTemplateID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `clientAppID` bigint(20) unsigned NOT NULL,
  `taskQueueID` bigint(20) NOT NULL,
  `taskID` bigint(20) NOT NULL,
  `tweetID` varchar(100) NOT NULL,
  `tweet` text NOT NULL,
  `author` varchar(100) DEFAULT NULL,
  `lat` varchar(100) DEFAULT NULL,
  `lon` varchar(100) DEFAULT NULL,
  `url` varchar(300) DEFAULT NULL,
  `created` varchar(150) DEFAULT NULL,
  `answer` text NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`reportTemplateID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2961 ;

-- --------------------------------------------------------

--
-- Table structure for table `taskLog`
--

CREATE TABLE `taskLog` (
  `taskQueueID` bigint(20) unsigned NOT NULL,
  `status` int(10) unsigned NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`taskQueueID`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `taskQueue`
--

CREATE TABLE `taskQueue` (
  `taskQueueID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `taskID` bigint(20) DEFAULT NULL,
  `clientAppID` bigint(20) unsigned NOT NULL,
  `documentID` bigint(20) DEFAULT NULL,
  `clientAppSourceID` bigint(20) DEFAULT NULL,
  `status` int(11) unsigned NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`taskQueueID`,`clientAppID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=86993 ;

-- --------------------------------------------------------

--
-- Table structure for table `taskQueueResponse`
--

CREATE TABLE `taskQueueResponse` (
  `taskQueueID` bigint(20) NOT NULL,
  `response` text NOT NULL,
  `taskInfo` text,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`taskQueueID`),
  UNIQUE KEY `taskQueueID_UNIQUE` (`taskQueueID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
