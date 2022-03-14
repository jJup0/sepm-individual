-- insert initial test data
-- the IDs are hardcoded to enable references between further test data
-- negative IDs are used to not interfere with user-entered data and allow clean deletion of test data

DELETE
FROM horse
where id < 0;

INSERT INTO horse (id, name, description, birthdate, sex, owner, mother, father)
VALUES
    (-1, 'Wendy', 'first horse', '2000-01-01', 'female', '', null, null),
    (-2, 'Alex', '', '2001-01-01', 'male', '', null, null),
    (-3, 'Lilly', '', '2002-01-01', 'female', '', -1, -2),
    (-4, 'Alexia', '', '2003-01-01', 'female', '', null, null),
    (-5, 'Fancy', '', '2004-01-01', 'male', '', null, null),
    (-6, 'Sugar', '', '2005-01-01', 'male', '', -4, -5),
    (-7, 'Lady', '', '2006-01-01', 'female', '', -3, -6),
    (-8, 'Tucker', '', '2007-01-01', 'male', '', null, null),
    (-9, 'Dakota', '', '2008-01-01', 'male', '', null, null),
    (-10, 'Cash', '', '2009-01-01', 'male', '', null, null)
;
DELETE
FROM owner
where id < 0;

INSERT INTO owner (id, firstName, lastName, email)
VALUES
    (-1, 'Owner 1', 'Lastname 1', 'jeff@example.com'),
    (-2, 'Owner 2', 'Lastname 2', 'chef@example.com'),
    (-3, 'Owner 3', 'Lastname 3', 'mr@example.com'),
    (-4, 'Owner 4', 'Lastname 4', 'hello@example.com')
;


