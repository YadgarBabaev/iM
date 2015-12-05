package mmsolutions.im;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "MyDB";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create databases
        db.execSQL("CREATE TABLE IF NOT EXISTS category (id integer primary key, name text);");
        db.execSQL("CREATE TABLE IF NOT EXISTS sub_category (id integer primary key, name text, " +
                "parent_id integer, FOREIGN KEY(parent_id) REFERENCES category(id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS sub_sub_category (id integer primary key, name text, " +
                "parent_id integer, FOREIGN KEY(parent_id) REFERENCES sub_category(id));");

        //insert data
        //category
        db.execSQL("INSERT INTO category VALUES (1, 'Одежда');");
        db.execSQL("INSERT INTO category VALUES (2, 'Обувь');");
        db.execSQL("INSERT INTO category VALUES (3, 'Аксессуары');");
        db.execSQL("INSERT INTO category VALUES (4, 'В подарок');");
        db.execSQL("INSERT INTO category VALUES (5, 'Детский мир');");
        db.execSQL("INSERT INTO category VALUES (6, 'Спорт и активный образ жизни');");
        db.execSQL("INSERT INTO category VALUES (7, 'Все для дома');");
        db.execSQL("INSERT INTO category VALUES (8, 'Техника');");
        db.execSQL("INSERT INTO category VALUES (9, 'Стройматериалы');");
        //sub_category
        db.execSQL("INSERT INTO sub_category VALUES (2, 'Мужские', 1);");
        db.execSQL("INSERT INTO sub_category VALUES (8, 'Женские', 1);");
        db.execSQL("INSERT INTO sub_category VALUES (9, 'Детские', 1);");
        db.execSQL("INSERT INTO sub_category VALUES (10, 'Мужская', 2);");
        db.execSQL("INSERT INTO sub_category VALUES (11, 'Женская', 2);");
        db.execSQL("INSERT INTO sub_category VALUES (12, 'Детская', 2);");
        db.execSQL("INSERT INTO sub_category VALUES (14, 'Женские сумки', 3);");
        db.execSQL("INSERT INTO sub_category VALUES (15, 'Чемоданы и дорожные сумки', 3);");
        db.execSQL("INSERT INTO sub_category VALUES (16, 'Спортивные сумки', 3);");
        db.execSQL("INSERT INTO sub_category VALUES (17, 'Кошельки и клатчи', 3);");
        db.execSQL("INSERT INTO sub_category VALUES (18, 'Сувениры', 4);");
        db.execSQL("INSERT INTO sub_category VALUES (19, 'Свадебные товары', 4);");
        db.execSQL("INSERT INTO sub_category VALUES (20, 'Товары для ухода за ребенком', 5);");
        db.execSQL("INSERT INTO sub_category VALUES (21, 'Товары для будущих мам', 5);");
        db.execSQL("INSERT INTO sub_category VALUES (22, 'Игрушки', 5);");
        db.execSQL("INSERT INTO sub_category VALUES (23, 'Аксессуары для ребенка', 5);");
        db.execSQL("INSERT INTO sub_category VALUES (24, 'Детское питание', 5);");
        db.execSQL("INSERT INTO sub_category VALUES (25, 'Спортивный инвентарь', 6);");
        db.execSQL("INSERT INTO sub_category VALUES (26, 'Спортивное питание', 6);");
        db.execSQL("INSERT INTO sub_category VALUES (27, 'Тренажеры', 6);");
        db.execSQL("INSERT INTO sub_category VALUES (28, 'Мягкая мебель', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (29, 'Корпусная мебель', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (30, 'Офисная мебель', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (31, 'Специализированная мебель', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (32, 'Постельное бельё, текстиль для дома', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (33, 'Посуда, кухонные принадлежности', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (34, 'Декор интерьера', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (35, 'Сантехника', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (36, 'Осветительная техника', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (37, 'Окна, витражи и подоконники', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (38, 'Двери', 7);");
        db.execSQL("INSERT INTO sub_category VALUES (39, 'Компьютеры, ноутбуки, оргтехника', 8);");
        db.execSQL("INSERT INTO sub_category VALUES (40, 'Аудио-, видеотехника', 8);");
        db.execSQL("INSERT INTO sub_category VALUES (41, 'Фото, видео, оптика', 8);");
        db.execSQL("INSERT INTO sub_category VALUES (42, 'Бытовая техника', 8);");
        db.execSQL("INSERT INTO sub_category VALUES (43, 'Мобильные телефоны и планшеты', 8);");
        db.execSQL("INSERT INTO sub_category VALUES (44, 'Гаджеты', 8);");
        db.execSQL("INSERT INTO sub_category VALUES (45, 'Музыкальные инструменты', 8);");
        db.execSQL("INSERT INTO sub_category VALUES (46, 'Строительные, отделочные материалы', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (47, 'Инструменты', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (48, 'Лакокрасочные изделия', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (49, 'Клеи и вяжущие составы', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (50, 'Сухие смеси и грунтовки', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (51, 'Сыпучие материалы', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (52, 'Кровельные материалы', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (53, 'Гидроизоляция', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (54, 'Крепежи, гвозди и болты', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (55, 'Металлопрокат', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (56, 'Металлоконструкция', 9);");
        db.execSQL("INSERT INTO sub_category VALUES (57, 'Товары для туризма', 6);");
        //sub_sub_category
        db.execSQL("INSERT INTO sub_sub_category VALUES (1, 'Футболки', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (2, 'Кофты', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (3, 'Рубашки', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (4, 'Джинсы', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (5, 'Пиджаки', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (6, 'Классические костюмы', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (7, 'Белье', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (8, 'Плащи и пальто', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (9, 'Шорты', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (10, 'Джемперы', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (11, 'Куртки', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (12, 'Платья и сарафаны', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (13, 'Нижнее белье', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (14, 'Джемперы и кардиганы', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (15, 'Рубашки и блузки', 2);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (16, 'Верхняя одежда', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (17, 'Джинсы и брюки', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (18, 'Футболки', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (19, 'Шорты', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (20, 'Купальники', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (21, 'Одежда для дома', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (22, 'Свадебные платья', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (23, 'Юбки', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (24, 'Куртки', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (25, 'Пиджаки и жакеты', 8);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (26, 'Кофты', 9);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (27, 'Для новорожденных', 9);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (28, 'Платья и юбки', 9);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (29, 'Костюмы', 9);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (30, 'Игрушки', 9);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (31, 'Брюки', 9);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (32, 'Куртки', 9);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (33, 'Футболки и майки', 9);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (34, 'Белье', 9);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (35, 'Ботинки', 10);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (36, 'Кроссовки и кеды', 10);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (37, 'Летняя обувь', 10);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (38, 'Мокасины и сандали', 10);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (39, 'Сапоги', 10);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (40, 'Туфли', 10);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (41, 'Балетки', 11);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (42, 'Босоножки', 11);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (43, 'Ботильоны', 11);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (44, 'Ботинки', 11);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (45, 'Кросовки', 11);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (46, 'Пляжная обувь', 11);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (47, 'Полусапожки', 11);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (48, 'Сапоги', 11);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (49, 'Туфли', 11);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (50, 'Для мальчиков', 12);");
        db.execSQL("INSERT INTO sub_sub_category VALUES (51, 'Для девочек', 12);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS sub_category");
        db.execSQL("DROP TABLE IF EXISTS sub_sub_category");
        onCreate(db);
    }

    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM category";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        return labels;
    }
    public List<String> getAllLabelsByParent(String table, int parent_id){
        List<String> labels = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + table + " WHERE parent_id = " + parent_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do{ labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        return labels;
    }
    public int getId(String table, String name){
        String selectQuery = "SELECT * FROM " + table + " WHERE name = '" + name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        db.close();
        return id;
    }
}
