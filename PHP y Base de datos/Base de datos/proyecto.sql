-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-03-2019 a las 17:57:33
-- Versión del servidor: 10.1.37-MariaDB
-- Versión de PHP: 7.3.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `proyecto`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `correo`
--

CREATE TABLE `correo` (
  `Id_Usuario_Envia` int(12) NOT NULL,
  `Mensaje` int(254) NOT NULL,
  `ID_Usuario_Objetivo` int(12) NOT NULL,
  `Estado` enum('Enviado','Sin Enviar','Respuesta') COLLATE latin1_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuenta`
--

CREATE TABLE `cuenta` (
  `Id_Usuario` int(12) NOT NULL,
  `Correo` varchar(60) COLLATE latin1_spanish_ci DEFAULT NULL,
  `Contrasenia` varchar(60) COLLATE latin1_spanish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

--
-- Volcado de datos para la tabla `cuenta`
--

INSERT INTO `cuenta` (`Id_Usuario`, `Correo`, `Contrasenia`) VALUES
(2, '23', '23'),
(3, 'a', '1'),
(4, 'aa', 'a'),
(5, 'agonzalezc@ciudaddelosmuchachos.com', '1234'),
(6, 'ser@gmail.com', '1234'),
(7, 'pp', 'pp'),
(8, 'v@gmail.com', '1'),
(9, '', '1'),
(10, 'x@x.com', '123'),
(11, '123@x.com', '123'),
(12, 'xa@xa.com', 'xa'),
(13, 'xs@pepe.com', 'xs');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `enfermo`
--

CREATE TABLE `enfermo` (
  `Id_Usuario` int(12) NOT NULL,
  `ID` int(12) NOT NULL,
  `Nombre` varchar(40) COLLATE latin1_spanish_ci NOT NULL,
  `Apellido` varchar(40) COLLATE latin1_spanish_ci NOT NULL,
  `Telefono_Usuario` int(9) NOT NULL,
  `QR` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `externo`
--

CREATE TABLE `externo` (
  `Id_Usuario` int(12) NOT NULL,
  `Id_Enfermo` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `localizacion`
--

CREATE TABLE `localizacion` (
  `ID_Enfermo` int(12) NOT NULL,
  `Lugar` varchar(90) COLLATE latin1_spanish_ci NOT NULL,
  `Fecha` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permisos`
--

CREATE TABLE `permisos` (
  `ID_Enfermo` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `ID` int(12) NOT NULL,
  `Nombre` varchar(40) COLLATE latin1_spanish_ci NOT NULL,
  `Apellido` varchar(40) COLLATE latin1_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`ID`, `Nombre`, `Apellido`) VALUES
(1, 'Prueba', 'Prueba'),
(2, '23', '23'),
(3, 'sergio', 's'),
(4, 'a', 'a'),
(5, 'Adrian', 'Gonzalez'),
(6, 'Sergio', 'sanz llamas'),
(7, 'pp', 'pp'),
(8, 'v', 'v'),
(9, 'JosÃ© Ignacio', 'Ignacio'),
(10, 'x', ''),
(11, '123', '123'),
(12, 'xa', 'xa'),
(13, 'xs', 'xs');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cuenta`
--
ALTER TABLE `cuenta`
  ADD PRIMARY KEY (`Id_Usuario`),
  ADD UNIQUE KEY `Correo` (`Correo`);

--
-- Indices de la tabla `enfermo`
--
ALTER TABLE `enfermo`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Telefono` (`Telefono_Usuario`),
  ADD KEY `Id_Usuario_Enfermo` (`Id_Usuario`);

--
-- Indices de la tabla `externo`
--
ALTER TABLE `externo`
  ADD KEY `Id_enfermo` (`Id_Enfermo`),
  ADD KEY `Id_Usuario_externo` (`Id_Usuario`);

--
-- Indices de la tabla `localizacion`
--
ALTER TABLE `localizacion`
  ADD PRIMARY KEY (`ID_Enfermo`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `enfermo`
--
ALTER TABLE `enfermo`
  MODIFY `ID` int(12) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `ID` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cuenta`
--
ALTER TABLE `cuenta`
  ADD CONSTRAINT `Id_Usuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `enfermo`
--
ALTER TABLE `enfermo`
  ADD CONSTRAINT `Id_Usuario_Enfermo` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `externo`
--
ALTER TABLE `externo`
  ADD CONSTRAINT `Id_Usuario_externo` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Id_enfermo` FOREIGN KEY (`Id_Enfermo`) REFERENCES `enfermo` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `localizacion`
--
ALTER TABLE `localizacion`
  ADD CONSTRAINT `Enfermo_ID` FOREIGN KEY (`ID_Enfermo`) REFERENCES `enfermo` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
