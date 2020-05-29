<?php

  // Getting database connection
  $server_name = "localhost";
  $username = "root";
  $password = "";
  $database = "firstdb";

  $connection = mysqli_connect($server_name, $username, $password, $database);

  // If there is any error while connecting to the database,
  // then stop the further execution and display error message
  if($connection -> connect_error) die("Connection failed: ".$connection->connect_error);

  $response = array();

  if (isset($_GET['enbapicall'])){
    switch ($_GET['enbapicall']) {
        case 'login':
            if(parametersAvailable(array('email','password'))) {
                $email = $_POST['email'];
                $password = md5($_POST['password']);

                // Database query
                $statement = $connection -> prepare("SELECT id, username, email, created_at, updated_at, gender FROM users WHERE email = ? and password = ?");
                $statement -> bind_param("ss", $email, $password);
                $statement -> execute();
                $statement -> store_result();

                if($statement -> num_rows > 0) {
                    $statement -> bind_result($id, $username, $email, $created_at, $updated_at, $gender);
                    $statement -> fetch();

                    $user = array(
                        'id' => $id,
                        'username' => $username,
                        'email' => $email,
                        'created_at' => $created_at,
                        'updated_at' => $updated_at,
                        'gender' => $gender);

                    $response['error'] = false;
                    $response['message'] = 'Login successful.';
                    $response['user'] = $user;
                    $statement -> close();
                } else {
                    $response['error'] = true;
                    $response['message'] = 'Invalid email or password.';
                }

            } else {
                $response['error'] = true;
                $response['message'] = 'Required parameters email or password missing.';
            }
           break;

        case 'signup':
            if(parametersAvailable(array('email','password', 'username'))) {
                $email = $_POST['email'];

                $statement = $connection -> prepare("SELECT * FROM users WHERE email = ?");
                $statement->bind_param("s", $email);

                $statement -> execute();
                $statement -> store_result();
                if($statement -> num_rows > 0) {
                    $response['error'] = true;
                    $response['message'] = 'Email already registered. Please try login to continue.';
                } else {
                    $username = $_POST['username'];
                    $password = md5($_POST['password']);
                    $statement = $connection -> prepare("INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
                    $statement -> bind_param("sss", $username, $email, $password);

                    if($statement -> execute()) {
                        $statement = $connection -> prepare("SELECT id, username, email, created_at, updated_at, gender FROM users WHERE email = ?");
                        $statement -> bind_param("s", $email);
                        $statement -> execute();
                        $statement -> bind_result($id, $username, $email, $created_at, $updated_at, $gender);
                        $statement -> fetch();

                        $user = array(
                            'id' => $id,
                            'username' => $username,
                            'email' => $email,
                            'created_at' => $created_at,
                            'updated_at' => $updated_at,
                            'gender' => $gender);

                        $response['error'] = false;
                        $response['message'] = 'User registered successfully.';
                        $response['user'] = $user;
                    }
                    $statement -> close();
                }
             } else {
                $response['error'] = true;
                $response['message'] = 'Required parameters missing. Please make sure that username, email and password available for signup execution.';
             }
           break;
        case 'notes':
            if(parametersAvailable(array('userId'))) {
                $userId = $_POST['userId'];

                // Database query
                $statement = $connection -> prepare("SELECT id, userId, content, created_at FROM notes WHERE userId = ?");
                $statement -> bind_param("s", $userId);
                $statement -> execute();
                $statement -> store_result();

                if($statement -> num_rows > 0) {
                    $statement -> bind_result($id, $userId, $content, $created_at);
                    $notes = array();
                    while($statement -> fetch()) {
                        $note = array(
                            'id' => $id,
                            'userId' => $userId,
                            'content' => $content,
                            'created_at' => $created_at);
                        // push single note into final response array
                        array_push($notes, $note);
                     }

                    $response['error'] = false;
                    $response['message'] = 'Execution successful.';
                    $response['notes'] = $notes;
                    $statement -> close();
                } else {
                    $response['error'] = true;
                    $response['message'] = 'User has no any notes yet.';
                }
            } else {
                $response['error'] = true;
                $response['message'] = 'Required parameter userId is missing.';
            }
           break;

		case 'insertnote':
		    if(parametersAvailable(array('userId', 'content', 'created_at'))) {
		        $userId = $_POST['userId'];
		        $content = $_POST['content'];
		        $created_at = $_POST['created_at'];
		        $statement = $connection -> prepare("INSERT INTO notes (userId, content, created_at) VALUES (?, ?, ?)");
		        $statement -> bind_param("sss", $userId, $content, $created_at);

		         if($statement -> execute()) {
                    $response['error'] = false;
                    $response['message'] = 'New note created in web db successfully.';
                 } else {
                    $response['error'] = true;
                    $response['message'] = 'Cannot create new note in web database. Error: '. $statement->error;
                 }
                 $statement -> close();
            } else {
                $response['error'] = true;
                $response['message'] = 'Required parameter(s) missing. Please make sure that userId, note content and note created_at parameters available.';
            }
           break;

        default:
            $response['error'] = true;
            $response['message'] = 'Invalid execution... API should be called for login, signup or notes operations.';
    }

  } else {
       $response['error'] = true;
       $response['message'] = 'Invalid API call';
  }

  header('Content-Type:Application/json');
  echo json_encode($response);

  function parametersAvailable($params) {
    foreach($params as $param) { if(!isset($_POST[$param])) return false; }
    return true;
  }

?>