<?php
$data = array('name' => 'Study', 'description' => 'Study notes');
$options = array(
    'http' => array(
        'header'  => "Content-type: application/json\r\nAccept: application/json\r\n",
        'method'  => 'POST',
        'content' => json_encode($data),
        'ignore_errors' => true
    )
);
$context  = stream_context_create($options);
$result = file_get_contents('http://127.0.0.1:8000/api/categories', false, $context);
echo "POST /api/categories:\n$result\n\n";

$data = array('category_id' => 1, 'title' => 'Math', 'content' => 'Math notes');
$options['http']['content'] = json_encode($data);
$context  = stream_context_create($options);
$result = file_get_contents('http://127.0.0.1:8000/api/notes', false, $context);
echo "POST /api/notes:\n$result\n\n";

$result = file_get_contents('http://127.0.0.1:8000/api/categories', false, stream_context_create(['http' => ['ignore_errors' => true]]));
echo "GET /api/categories:\n$result\n\n";

$result = file_get_contents('http://127.0.0.1:8000/api/notes', false, stream_context_create(['http' => ['ignore_errors' => true]]));
echo "GET /api/notes:\n$result\n\n";

$options['http']['method'] = 'DELETE';
$options['http']['content'] = '';
$context = stream_context_create($options);
$result = file_get_contents('http://127.0.0.1:8000/api/categories/1', false, $context);
echo "DELETE /api/categories/1:\n$result\n\n";
