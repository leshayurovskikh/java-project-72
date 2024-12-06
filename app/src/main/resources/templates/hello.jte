<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <title>Приветствие</title>
</head>
<body>
<header class="p-3 text-bg-dark">
    <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-start justify-content-lg-start">
            <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                <svg class="bi me-2" width="40" height="32" role="img" aria-label="Bootstrap"><use xlink:href="#bootstrap"></use></svg>
            </a>

            <style>
                .large-text {
                    font-size: 2.5rem; /* Измените значение, чтобы настроить размер */
                }
                .nav {
           text-align: left;
       }
                .form-control {
        width: 700px; /* Установите нужную ширину поля запроса */
            </style>

            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 mb-md-0 justify-content-start text-start">
                <li><a href="/" class="nav-link large-text text-white">Анализатор страниц</a></li>
                <li><a href="/" class="nav-link px-2 text-secondary">Главная</a></li>
                <li><a href="#" class="nav-link px-2 text-secondary">Сайты</a></li>
            </ul>
            <br>
            <h1>Введите адрес</h1>
            <div>
            <form class="control" action="/urls" method="POST">
                <input type="text" name="address" placeholder="Введите URL" required>
                <button type="submit">Отправить</button>
            </form>
                </div>
        </div>
    </div>
</header>
</body>
</html>
