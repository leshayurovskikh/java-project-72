<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Hello Hexlet!</title>
</head>
<body>
<div class="col-lg-8 mx-auto p-4 py-md-5">
    <main>
        <h1 class="text-body-emphasis">Привет, Хекслет!</h1>
        <h1>Введите адрес:</h1>
        <form action="/urls" method="POST">
            <label for="address">Адрес:</label>
            <input type="text" id="address" name="address" required>
            <button type="submit">Отправить</button>
        </form>
    </main>
</div>
</body>
</html>