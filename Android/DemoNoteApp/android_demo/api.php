<?php

  // Getting database connection
  $server_name = "localhost";
  $username = "root";
  $password = "";
  $database = "firstdb";

  $connection = mysqli_connect($server_name, $username, $password, $database) or die("Connection could not established!");
  // If there is any error while connecting to the database,
  // then stop the further execution and display error message
  if($connection -> connect_error) die("Connection failed: ".$connection->connect_error);

  $response = array();
  $target_dir = "images/";

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
                $statement = $connection -> prepare("SELECT id, userId, content, created_at, updated_at FROM notes WHERE userId = ?");
                $statement -> bind_param("s", $userId);
                $statement -> execute();
                $statement -> store_result();

                if($statement -> num_rows > 0) {
                    $statement -> bind_result($id, $userId, $content, $created_at, $updated_at);
                    $notes = array();
                    while($statement -> fetch()) {
                        $note = array(
                            'id' => $id,
                            'userId' => $userId,
                            'content' => $content,
                            'created_at' => $created_at,
                            'updated_at' => $updated_at);
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
		case 'updatenote':
		    if(parametersAvailable(array('id', 'userId', 'content', 'updated_at'))) {
		        $id = $_POST['id'];
		        $userId = $_POST['userId'];
		        $content = $_POST['content'];
		        $updated_at = $_POST['updated_at'];
		        $statement = $connection -> prepare("UPDATE notes SET content = ?, updated_at = ? WHERE id = ? and userId = ?");
		        $statement -> bind_param("ssss", $content, $updated_at, $id, $userId);

		         if($statement -> execute()) {
                    $response['error'] = false;
                    $response['message'] = 'Note updated in web db successfully.';
                 } else {
                    $response['error'] = true;
                    $response['message'] = 'Cannot update note in web database. Error: '. $statement->error;
                 }
                 $statement -> close();
            } else {
                $response['error'] = true;
                $response['message'] = 'Required parameter(s) missing. Please make sure that userId, note content and note created_at parameters available.';
            }
           break;

		case 'deletenote':
		    if(parametersAvailable(array('id', 'userId'))) {
		        $id = $_POST['id'];
		        $userId = $_POST['userId'];
		        $statement = $connection -> prepare("DELETE FROM notes WHERE id = ? and userId = ?");
		        $statement -> bind_param("ss", $id, $userId);

		         if($statement -> execute()) {
                    $response['error'] = false;
                    $response['message'] = 'Note deleted from web database successfully.';
                 } else {
                    $response['error'] = true;
                    $response['message'] = 'Cannot delete note in web database. Error: '. $statement->error;
                 }
                 $statement -> close();
            } else {
                $response['error'] = true;
                $response['message'] = 'Required parameter(s) missing. Please make sure that id and userId parameters available.';
            }
           break;

        case 'upload':
            if(parametersAvailable(array('userId'))) {
                $userId = $_POST['userId'];

                $filename= $_FILES["image"]["name"];
                $filename = preg_replace('/\s+/', '_', $filename);
                // echo $filename.'\n';

                $target_file = $target_dir.uniqid().'_'.$filename;
                // echo $target_file;

                // file will be uploaded to server tmp directory
                if(move_uploaded_file($_FILES['image']['tmp_name'], $target_file)) {
                    // if file uploaded to server successfully, then insert its path to MYSQL database.
                    $statement = $connection -> prepare("UPDATE users SET path = ? WHERE id = ?");
                    $statement -> bind_param("ss", $target_file, $userId);

                    if($statement->execute()){
                        $response['error'] = false;
                        $response['message'] = 'User image updated successfully.';
                        $response['image'] = getBaseURL() . $target_file;
                    } else {
                        $response['error'] = true;
                        $response['message'] = 'Cannot update user image in MYSQL database. Error: '. $statement->error;
                    }

                    $statement-> close();
                } else {
                    $response['error'] = true;
                    $response['message'] = 'Uploading file to ' . $target_dir . ' directory is failed. Please try again later.';
                }
            } else {
                $response['error'] = true;
                $response['message'] = 'Required parameters missing. Please make sure that userId and image parameters available for upload execution.';
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

  function parametersAvailable($params) {
    foreach($params as $param) { if(!isset($_POST[$param])) return false; }
    return true;
  }

  function getBaseURL(){
    $url = isset($_SERVER['HTTPS']) ? 'https://' : 'http://';
    $url .= $_SERVER['SERVER_NAME'];
    $url .= $_SERVER['REQUEST_URI'];
    return dirname($url) . '/';
  }

  header('Content-Type:Application/json');
  echo json_encode($response);
?>