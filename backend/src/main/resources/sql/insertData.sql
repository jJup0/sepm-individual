-- insert initial test data
-- the IDs are hardcoded to enable references between further test data
-- negative IDs are used to not interfere with user-entered data and allow clean deletion of test data

DELETE
FROM horse
where id < 0;

INSERT INTO horse (id, name, description, birthdate, sex, owner)
VALUES
    (-1, 'Wendy', 'first horse', '2000-01-01', 'female', ''),
    (-2, 'Alex', '', '2000-01-01', 'male', ''),
    (-3, 'Lilly', '', '2000-01-01', 'female', ''),
    (-4, 'Alexia', '', '2000-01-01', 'female', ''),
    (-5, 'Fancy', '', '2000-01-01', 'male', ''),
    (-6, 'Sugar', '', '2000-01-01', 'male', ''),
    (-7, 'Lady', '', '2000-01-01', 'female', ''),
    (-8, 'Tucker', '', '2000-01-01', 'male', ''),
    (-9, 'Dakota', '', '2000-01-01', 'male', ''),
    (-10, 'Cash', '', '2000-01-01', 'male', '')
;


