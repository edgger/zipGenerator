### Приложение для генерации zip-файлов, с необходимым размером.
#### Запуск:
`java -jar zipGenerator.jar` и ввод каждого параметра в консоль

Или
 
`java -jar zipGenerator.jar абсолютный/путь/к/директории/для/сохранения размерФайлов(КБайт) количество генерация.ack(true/false)`

#### Пример:
`java -jar zipGenerator.jar D:/temp/tstdir 512000 3 true`

Будет создано 3 файла, размером 500 МБайт, в директории D:/temp/tstdir и для каждого файл-метка .ack

###### Zip т.к. есть CRC. Без компрессии. В архивах находятся .txt файлы, размером по 1 Мбайт, с рандомным содержимым (повторяющийся 1 Кбайт).