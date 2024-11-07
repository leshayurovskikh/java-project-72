<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Приветствие</title>
</head>
<body>
<h1>Главная страница</h1>
<h1>Введите адрес:</h1>
<form action="/urls" method="POST">
    <label for="address">Адрес:</label>
    <input type="text" id="address" name="address" required>
    <button type="submit">Отправить</button>
</form>
</body>
</html>