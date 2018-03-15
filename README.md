# KaiBookDownloader
Позволяет работать с книгами с сайта e-library.kai.ru в офлайн режиме.

Для сборки рекомендуется maven 3.3.9 и JDK 1.8 и выше.

Склонируйте проект и в его корне проекта выполните команду: 
`mvn clean package`.

В папке target должен появиться исполняемый .jar файл. Н-р, `KaiBookDownloader-1.0-SNAPSHOT-jar-with-dependencies.jar`.

Для его запуска используйте команду:

`java -jar <путь к .jar файлу> -id <id книги> -f <название выходного файла>`.

Пример:

`java -jar ./target/KaiBookDownloader-1.0-SNAPSHOT-jar-with-dependencies.jar -id 1572 -f ТПР`

