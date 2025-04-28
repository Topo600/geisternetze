-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 28. Apr 2025 um 15:50
-- Server-Version: 10.4.32-MariaDB
-- PHP-Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `shepherd`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `geisternetz`
--

CREATE TABLE `geisternetz` (
  `id` int(11) NOT NULL,
  `bgrad` decimal(38,2) DEFAULT NULL,
  `lgrad` decimal(38,2) DEFAULT NULL,
  `flaeche` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `meldendePersonId` int(11) DEFAULT NULL,
  `bergendePersonId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `geisternetz`
--

INSERT INTO `geisternetz` (`id`, `bgrad`, `lgrad`, `flaeche`, `status`, `meldendePersonId`, `bergendePersonId`) VALUES
(1, 10.10, 10.10, 10, 0, NULL, NULL),
(2, 11.10, 11.10, 10, 1, 1, 3),
(3, 12.10, 12.10, 10, 2, NULL, 3);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `person`
--

CREATE TABLE `person` (
  `id` int(11) NOT NULL,
  `vorname` varchar(25) NOT NULL,
  `nachname` varchar(25) NOT NULL,
  `telefonnummer` varchar(20) DEFAULT NULL,
  `funktion` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `person`
--

INSERT INTO `person` (`id`, `vorname`, `nachname`, `telefonnummer`, `funktion`) VALUES
(1, 'Anna', 'Musterfrau', '123456', NULL),
(2, 'Max', 'Mustermann', '56789', 1),
(3, 'Martin', 'Musterperson', '654321', 1);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `geisternetz`
--
ALTER TABLE `geisternetz`
  ADD PRIMARY KEY (`id`),
  ADD KEY `meldendePersonId` (`meldendePersonId`),
  ADD KEY `bergendePersonId` (`bergendePersonId`);

--
-- Indizes für die Tabelle `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `geisternetz`
--
ALTER TABLE `geisternetz`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT für Tabelle `person`
--
ALTER TABLE `person`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `geisternetz`
--
ALTER TABLE `geisternetz`
  ADD CONSTRAINT `geisternetz_ibfk_1` FOREIGN KEY (`meldendePersonId`) REFERENCES `person` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `geisternetz_ibfk_2` FOREIGN KEY (`bergendePersonId`) REFERENCES `person` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
