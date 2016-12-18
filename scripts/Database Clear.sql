DECLARE
  prod_exists NUMBER;
  test_exists NUMBER;
BEGIN
  SELECT count(1)
  INTO prod_exists
  FROM dba_users
  WHERE username = upper('c##prod');
  IF prod_exists != 0
  THEN
    EXECUTE IMMEDIATE ('drop user c##prod cascade');
  END IF;

  SELECT count(1)
  INTO test_exists
  FROM dba_users
  WHERE username = upper('c##test');
  IF prod_exists != 0
  THEN
    EXECUTE IMMEDIATE ('drop user c##test cascade');
  END IF;
END;