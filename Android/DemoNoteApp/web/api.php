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

  if (isset($_GET['apicall'])){
    switch ($_GET['apicall']) {
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
        default:
            $response['error'] = true;
            $response['message'] = 'Invalid execution... API should be called for login or signup operations.';
    }

  } else {
       $response['error'] = true;
       $response['message'] = 'Invalid API call';
  }

  echo json_encode($response);

  function parametersAvailable($params) {
    foreach($params as $param) { if(!isset($_POST[$param])) return false; }
    return true;
  }

?>