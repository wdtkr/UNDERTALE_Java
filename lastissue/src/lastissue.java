import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.multi.MultiLabelUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Random;
import java.util.TimerTask;

public class lastissue extends JFrame {

    private JPanel panel1;
    private JButton fight;
    private JButton mercy;
    private JButton item;
    private JButton act;
    private JPanel field;
    private JTextArea messageText;
    private JLabel yourName;
    private JLabel myHP;
    private JLabel enemyImg;
    private JLabel myImg;
    private JPanel battle;
    private JLabel scorefield;
    private JPanel Startpanel;
    private JPanel panel;
    private JPanel Howtopanel;
    private JPanel Gameoverpanel;
    private JLabel enemy1;
    private JLabel enemy2;
    private JLabel enemy3;
    private JLabel enemy4;

    PointerInfo pi;
    Point pt;
//    マウスカーソルの位置を取得するもの

    Color orange = Color.decode("#FF9400");
    Color yellow = Color.decode("#F2E316");
//        カラーコードの宣言

    private String name = "あなた";

    private int pos_x = 0;
    private int pos_y = 0;
    private int pos_x_re = 0;
    private int pos_y_re = 0;
//    ハートの位置

    private int speed = 0;
    private int score = 0;
//    スピード（回をおうごとに早くなる）とスコア（生き残るとスコアが増える）

    private int myPoint = 20;
//    HP
    private int e_count1 = 0;
    private int e_count2 = 0;
    private int e_count3 = 0;
//    敵の動き始めを制御するカウント

    private int muteki_flag = 0;
//    無敵時間用フラッグ

    private Timer muteki_timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            muteki_flag = 0;
        }
    });
//    無敵時間用タイマー

    private int actionFlag = 0;
//    4つのボタンのいずれかをクリックした時用

    private int attackFlag = 0;
//    避けゲー状態のフラッグ

    private int mouseFlag = 0;
//    マウスをフィールド内で動かしてる時のフラッグ

    private int motionFlag = 0;
//    何のボタンを押したか判別する用　０が避ける　１が賭ける　２が回復

    static Timer timer1;
//     　　アップデート用

    static Timer timer2;
//        メッセージ表示用

    static Timer timer3;
//        戦闘時間用

    static Timer timer4;
//    逃げるを押した時用

    static Timer timer_e1;
    static Timer timer_e2;
    static Timer timer_e3;
//    敵の動き始めをずらすためのタイマー

    Font font;

    public String getName(){
//        一応作っとく
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public lastissue(){

//        ゲーム本編のプログラム（gameStart()）からコードを書いたので、そちらにコードの概要がより詳しく書かれています。

        // ウィンドウを閉じたら終了させる
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(Startpanel);
        Startpanel.setLayout(null);
        Startpanel.setBackground(Color.black);
        JButton st = new JButton();
        JButton asobikata = new JButton();
        JButton end = new JButton();

        BufferedImage hiro_img,sans_img;
//        リソースとして画像を読み込むことで、jarにしても画像が正しく読み込まれる

        try{
            hiro_img = ImageIO.read(this.getClass().getResource("img/hiroyuki.png"));
            Image image = hiro_img;
            Image newImg = image.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon icon;
            icon = new ImageIcon(newImg);
            JLabel hiro = new JLabel(icon);
            hiro.setBounds(240,650,200,200);
            Startpanel.add(hiro);

            sans_img = ImageIO.read(this.getClass().getResource("img/sans.png"));
            image = sans_img;
            newImg = image.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
            JLabel sans = new JLabel(icon);
            sans.setBounds(1000,650,200,200);
            Startpanel.add(sans);
        }catch (IOException e){
            System.out.println("画像読み込みエラー");
        }

        try{
            URL url = this.getClass().getResource("font/DeterminationJP.ttf");
            assert url != null;
            InputStream in = url.openStream();
            font = Font.createFont(Font.TRUETYPE_FONT,in).deriveFont(56f);
        }catch(FontFormatException e){
            System.out.println("形式がフォントではありません。");
        }catch(IOException e){
            System.out.println("入出力エラーでフォントを読み込むことができませんでした。");
        }catch (NullPointerException e){
            System.out.println("ファイルが読み込めません");
        }
        st.setFont(font);
        st.setBorder(new LineBorder(Color.white, 5, false));
        st.setForeground(Color.white);
        st.setBounds(470,400,500,200);
        st.setText("ゲームスタート");
        Startpanel.add(st);

//        ゲームスタートを押した時の処理
        st.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStart();
            }
        });

        asobikata.setFont(font);
        asobikata.setBorder(new LineBorder(Color.white, 5, false));
        asobikata.setForeground(Color.white);
        asobikata.setBounds(570,200,300,150);
        asobikata.setText("遊び方");
        Startpanel.add(asobikata);
        asobikata.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howto();
            }
        });

        end.setFont(font);
        end.setBorder(new LineBorder(Color.white, 5, false));
        end.setForeground(Color.white);
        end.setBounds(570,650,300,150);
        end.setText("やめる");
        Startpanel.add(end);
        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

//        ーーーーーーーーーーーーーーーーーーーー　マウスオーバー処理　ーーーーーーーーーーーーーーーーーーーーーーーーー

        asobikata.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                asobikata.setForeground(yellow);
//                  マウスオーバーした時にボタンの文字と枠を黄色にする
                asobikata.setBorder(new LineBorder(yellow, 5, false));
            }
        });

        st.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                st.setForeground(yellow);
//                  マウスオーバーした時にボタンの文字と枠を黄色にする
                st.setBorder(new LineBorder(yellow, 5, false));
            }
        });

        end.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                end.setForeground(yellow);
//                  マウスオーバーした時にボタンの文字と枠を黄色にする
                end.setBorder(new LineBorder(yellow, 5, false));
            }
        });

//        ーーーーーーーーーーーーーーーーーーーー　マウスアウト（？）処理　ーーーーーーーーーーーーーーーーーーーーーーーーー

        asobikata.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                asobikata.setForeground(Color.white);
                asobikata.setBorder(new LineBorder(Color.white, 5, false));
            }
        });

        st.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                st.setForeground(Color.white);
                st.setBorder(new LineBorder(Color.white, 5, false));
            }
        });

        end.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                end.setForeground(Color.white);
                end.setBorder(new LineBorder(Color.white, 5, false));
            }
        });

//        ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

    }

//    ーーーーーーーーーーーーーーーーーーーーーー　あそびかた画面　ーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

    public void howto(){
        Startpanel.setVisible(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(Howtopanel);
        Howtopanel.setLayout(null);
        Howtopanel.setBackground(Color.black);
        JButton ret = new JButton();
        JButton yoke = new JButton();
        JButton kake = new JButton();
        JButton kaihuku = new JButton();
        JButton nige = new JButton();

        JLabel yoke_info = new JLabel();
        JLabel kake_info = new JLabel();
        JLabel kaihuku_info = new JLabel();
        JLabel nige_info = new JLabel();

        try{
            URL url = this.getClass().getResource("font/DeterminationJP.ttf");
            InputStream in = url.openStream();
            font = Font.createFont(Font.TRUETYPE_FONT,in).deriveFont(36f);
        }catch(FontFormatException e){
            System.out.println("形式がフォントではありません。");
        }catch(IOException e){
            System.out.println("入出力エラーでフォントを読み込むことができませんでした。");
        }

        yoke_info.setBounds(400,100,1000,100);
        yoke_info.setFont(font);
        yoke_info.setForeground(Color.white);
        yoke_info.setText("マウスを動かして、敵の攻撃から避けよう。スコアが増えるぞ。");
        Howtopanel.add(yoke_info);
        yoke_info.setVisible(false);

        kake_info.setBounds(400,250,1000,100);
        kake_info.setFont(font.deriveFont(30f));
        kake_info.setForeground(Color.white);
        kake_info.setText("賭けに勝ったらスコアが更に増え、負けたら攻撃のスピードが早くなるぞ。");
        Howtopanel.add(kake_info);
        kake_info.setVisible(false);

        kaihuku_info.setBounds(400,400,1000,100);
        kaihuku_info.setFont(font.deriveFont(56f));
        kaihuku_info.setForeground(Color.white);
        kaihuku_info.setText("自分のHPが 5 回復するぞ。");
        Howtopanel.add(kaihuku_info);
        kaihuku_info.setVisible(false);

        nige_info.setBounds(400,550,1000,100);
        nige_info.setFont(font.deriveFont(32f));
        nige_info.setForeground(Color.white);
        nige_info.setText("ゲームを諦めて、逃走するぞ。逃走せず、ハイスコアを目指そう。");
        Howtopanel.add(nige_info);
        nige_info.setVisible(false);
        Howtopanel.add(nige_info);

        yoke.setFont(new Font("Arial Black",Font.PLAIN,36));
        yoke.setBorder(new LineBorder(orange, 5, false));
        yoke.setForeground(orange);
        yoke.setBounds(100,100,250,100);
        yoke.setText("よける");
        Howtopanel.add(yoke);
        yoke.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                yoke.setForeground(yellow);
//                  マウスオーバーした時にボタンの文字と枠を黄色にする
                yoke.setBorder(new LineBorder(yellow, 5, false));
                yoke_info.setVisible(true);
            }
        });
        yoke.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                yoke.setForeground(orange);
                yoke.setBorder(new LineBorder(orange, 5, false));
                yoke_info.setVisible(false);
            }
        });

        kake.setFont(new Font("Arial Black",Font.PLAIN,36));
        kake.setBorder(new LineBorder(orange, 5, false));
        kake.setForeground(orange);
        kake.setBounds(100,250,250,100);
        kake.setText("賭ける");
        Howtopanel.add(kake);
        kake.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                kake.setForeground(yellow);
//                  マウスオーバーした時にボタンの文字と枠を黄色にする
                kake.setBorder(new LineBorder(yellow, 5, false));
                kake_info.setVisible(true);
            }
        });
        kake.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                kake.setForeground(orange);
                kake.setBorder(new LineBorder(orange, 5, false));
                kake_info.setVisible(false);
            }
        });

        kaihuku.setFont(new Font("Arial Black",Font.PLAIN,36));
        kaihuku.setBorder(new LineBorder(orange, 5, false));
        kaihuku.setForeground(orange);
        kaihuku.setBounds(100,400,250,100);
        kaihuku.setText("かいふく");
        Howtopanel.add(kaihuku);
        kaihuku.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                kaihuku.setForeground(yellow);
//                  マウスオーバーした時にボタンの文字と枠を黄色にする
                kaihuku.setBorder(new LineBorder(yellow, 5, false));
                kaihuku_info.setVisible(true);
            }
        });
        kaihuku.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                kaihuku.setForeground(orange);
                kaihuku.setBorder(new LineBorder(orange, 5, false));
                kaihuku_info.setVisible(false);
            }
        });

        nige.setFont(new Font("Arial Black",Font.PLAIN,36));
        nige.setBorder(new LineBorder(orange, 5, false));
        nige.setForeground(orange);
        nige.setBounds(100,550,250,100);
        nige.setText("にげる");
        Howtopanel.add(nige);
        nige.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nige.setForeground(yellow);
//                  マウスオーバーした時にボタンの文字と枠を黄色にする
                nige.setBorder(new LineBorder(yellow, 5, false));
                nige_info.setVisible(true);
            }
        });
        nige.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                nige.setForeground(orange);
                nige.setBorder(new LineBorder(orange, 5, false));
                nige_info.setVisible(false);
            }
        });

        ret.setFont(font.deriveFont(56f));
        ret.setForeground(Color.white);
        ret.setBorder(new LineBorder(Color.white, 5, false));
        ret.setBounds(570,700,300,150);
        ret.setText("スタート");
        Howtopanel.add(ret);
        ret.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ret.setForeground(yellow);
//                  マウスオーバーした時にボタンの文字と枠を黄色にする
                ret.setBorder(new LineBorder(yellow, 5, false));
            }
        });
        ret.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                ret.setForeground(Color.white);
                ret.setBorder(new LineBorder(Color.white, 5, false));
            }
        });

        ret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStart();
            }
        });

    }

//    ーーーーーーーーーーーーーーーーーーーーーーーーーーー　ゲーム本編　ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

    public void gameStart(){
//        初期化（リトライしても大丈夫なように）
        speed = 0;
        myPoint = 20;
        score = 0;
        pos_x = 0;
        pos_y = 0;
        pos_x_re = 0;
        pos_y_re = 0;
        e_count1 = 0;
        e_count2 = 0;
        e_count3 = 0;
        muteki_flag = 0;
        actionFlag = 0;
        attackFlag = 0;
        mouseFlag = 0;
        motionFlag = 0;

        JPanel panel1 = new JPanel();
//        これが無いと、リトライした時に以前の描画が残ってしまう

        setContentPane(panel1);
        panel1.setBackground(Color.black);
        panel1.setLayout(null);
//        これをすると、座標で設定することが出来る

        panel.setVisible(false);

        fight.setBounds(100,750,250,100);
        fight.setForeground(orange);
//        文字をオレンジに
        fight.setBorder(new LineBorder(orange, 5, false));
//        枠をオレンジに
        panel1.add(fight);
//        追加
        act.setBounds(425,750,250,100);
        act.setForeground(orange);
        act.setBorder(new LineBorder(orange, 5, false));
        panel1.add(act);
        item.setBounds(725,750,250,100);
        item.setForeground(orange);
        item.setBorder(new LineBorder(orange, 5, false));
        panel1.add(item);
        mercy.setBounds(1050,750,250,100);
        mercy.setForeground(orange);
        mercy.setBorder(new LineBorder(orange, 5, false));
        panel1.add(mercy);
//        ボタンの座標での配置

        field.setBounds(100,400,1200,250);
        field.setOpaque(false);
//        パネルの背景を透過

        field.setBorder(new LineBorder(Color.white, 8, true));
        panel1.add(field);

        battle.setBounds(542,425,322,210);
        battle.setOpaque(false);
//        パネルの背景を透過
        panel1.add(battle);

        messageText.setOpaque(false);
        messageText.setEditable(false);
//        編集不可に
        messageText.setBounds(120,430,1200,250);
        messageText.setForeground(Color.white);
        try{
            URL url = this.getClass().getResource("font/DeterminationJP.ttf");
            InputStream in = url.openStream();
            font = Font.createFont(Font.TRUETYPE_FONT,in).deriveFont(56f);
        }catch(FontFormatException e){
            System.out.println("形式がフォントではありません。");
        }catch(IOException e){
            System.out.println("入出力エラーでフォントを読み込むことができませんでした。");
        }catch (NullPointerException e){
            System.out.println("入出力エラーでフォントを読み込むことができませんでした。");
        }
        messageText.setFont(font);
//        フォント読み込み
        messageText.setText("＊西村◯之　が現れた。");
        panel1.add(messageText);

        yourName.setBounds(100,575,400,250);
        yourName.setForeground(Color.white);
        yourName.setFont(font.deriveFont(42f));
        yourName.setText(name + "   LV 1");
        panel1.add(yourName);

        myHP.setBounds(550,575,400,250);
        myHP.setForeground(Color.white);
        myHP.setFont(font.deriveFont(36f));
        myHP.setText("HP   " + myPoint + " / 20");
        panel1.add(myHP);

        scorefield.setBounds(950,575,300,250);
        scorefield.setForeground(Color.white);
        scorefield.setFont(font.deriveFont(36f));
        scorefield.setText("スコア："+score);
        panel1.add(scorefield);

        BufferedImage img;

        try{
            img = ImageIO.read(this.getClass().getResource("img/nishimura.png"));
            Image image = img;
            ImageIcon icon;
            // 画像のサイズを変更
            Image newImg = image.getScaledInstance(300, 300,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
            enemyImg = new JLabel(icon);
            enemyImg.setBounds(570,90,300,300);
            panel1.add(enemyImg);
        }catch (IOException e){
            System.out.println("エラー：ファイルが読み込めませんでした。");
        }

        try{
            img = ImageIO.read(this.getClass().getResource("img/heart.png"));
            Image image = img;
            ImageIcon icon;
            // 画像のサイズを変更
            Image newImg = image.getScaledInstance(23, 23,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
            myImg = new JLabel(icon);
            myImg.setBounds(580,500,23,23);
            panel1.add(myImg);
            myImg.setVisible(false);
        }catch (IOException e){
            System.out.println("エラー：ファイルが読み込めませんでした。");
        }

        try{
            img = ImageIO.read(this.getClass().getResource("img/hato_bird.png"));
            Image image = img;
            ImageIcon icon;
            // 画像のサイズを変更
            Image newImg = image.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
            enemy1 = new JLabel(icon);
            enemy1.setBounds(900,409,100,100);
            panel1.add(enemy1);
            enemy1.setVisible(false);

            enemy2 = new JLabel(icon);
            enemy2.setBounds(900,600,100,100);
            panel1.add(enemy2);
            enemy2.setVisible(false);

            enemy3 = new JLabel(icon);
            enemy3.setBounds(900,500,100,100);
            panel1.add(enemy3);
            panel1.add(enemy3);
            enemy3.setVisible(false);

            enemy4 = new JLabel(icon);
            enemy4.setBounds(900,550,100,100);
            panel1.add(enemy4);
            enemy4.setVisible(false);
        }catch (IOException e){
            System.out.println("エラー：ファイルが読み込めませんでした。");
        }

        muteki_timer.setRepeats(false);

//        ーーーーーーーーーーーーーーーーーーーーー　アップデート処理　ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

        timer1 = new Timer(10, new ActionListener() {
            int width = 1200;
            int x = 100;
//             タイマーの設置と0.1秒度に実行される処理内容
            @Override
            public void actionPerformed(ActionEvent e) {
//                画面の描画
                myImg.setVisible(false);
                scorefield.setText("スコア："+score);
                if(mouseFlag == 1 ){
//                    System.out.println(pt);
                    pos_x = pt.x;
                    pos_y = pt.y;
                    pos_x_re = pos_x - 15;
                    pos_y_re = pos_y - 15;
                    myImg.setBounds(pos_x_re, pos_y_re,23,23);
                }
                if(attackFlag == 1 && field.getWidth() >= 380){
                    width -= 20;
                    x += 10;
                    field.setBounds(x,400,width,250);
                    panel1.add(field);
                }else if(attackFlag == 0 && field.getWidth() < 1200){
                    width += 20;
                    x -= 10;
                    field.setBounds(x,400,width,250);
                } else if(attackFlag == 1){
//                    ここでハートを動かして避けるゲームをする
                    myImg.setVisible(true);
                    enemy1.setVisible(true);
                    hantei();
                    score += 1;

                    pi = MouseInfo.getPointerInfo();
                    pt = pi.getLocation();
//                    System.out.println(pt);
//                    pt = () ;
                    if(pt.x < 542){
                        pt.x = 542;
                    }
                    if(pt.x > 864){
                        pt.x = 864;
                    }
                    if(pt.y < 425){
                        pt.y = 425;
                    }
                    if(pt.y > 635){
                        pt.y = 635;
                    }
//                    画面外にハートが飛び出したら、画面内に戻す else if は使わない。背反ではないので
//           　　　　メモ   542,425,322,210  x,y,w,h
                }
            }
        });

        //                    ーーーーーーーーーーーーーーー　メッセージ表示タイマー処理　ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
//        ３秒間メッセージを表示し、その後タイマー３がスタート
        timer2 = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("timer2");
                attackFlag = 1;
                messageText.setText("");
                timer3.start();
                Random rand = new Random();
                int num = rand.nextInt(250) + 400;
                enemy1.setBounds(900,num,100,100);
                num = rand.nextInt(250) + 400;
                enemy2.setBounds(900,num,100,100);
                num = rand.nextInt(250) + 400;
                enemy3.setBounds(900,num,100,100);
                num = rand.nextInt(250) + 400;
                enemy4.setBounds(900,num,100,100);
                e_count1 = 0;
                e_count2 = 0;
                e_count3 = 0;
            }
        });
        timer2.setRepeats(false);
//        複数回実行させないようにする

//                    ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

        //                    ーーーーーーーーーーーーーーー　戦闘タイマー処理　ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

        timer3 = new Timer(9000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("timer3");
                Random rand = new Random();
                int ra = rand.nextInt(5);
                switch (ra){
                    case 0:
                        messageText.setText("＊うそはうそであると見抜ける人でないと\n 　掲示板を使うのは難しい");
                        break;
                    case 1:
                        messageText.setText("＊”写像”・・・？なんすか？”写像”って");
                        break;
                    case 2:
                        messageText.setText("＊それってあなたの感想ですよね？");
                        break;
                    case 3:
                        messageText.setText("＊うそつくのやめてもらって\n 　いいですか？");
                        break;
                    case 4:
                        messageText.setText("＊なんかそういうデータあるんすか？");
                        break;
                }
                actionFlag = 0;
                attackFlag = 0;
                e_count1 = 0;
                e_count2 = 0;
                e_count3 = 0;
                enemy1.setVisible(false);
                enemy2.setVisible(false);
                enemy3.setVisible(false);
                enemy4.setVisible(false);
                speed++;
                if(score < 1000){
                    yourName.setText(name + "   LV 1");
                }else if(score < 2000){
                    yourName.setText(name + "   LV 2");
                }else if(score < 3000){
                    yourName.setText(name + "   LV 3");
                }else if(score < 4000){
                    yourName.setText(name + "   LV 4");
                }else if(score < 5000){
                    yourName.setText(name + "   LV 5");
                }else if(score < 10000){
                    yourName.setText(name + "   LV 6");
                }else if(score < 20000){
                    yourName.setText(name + "   LV 7");
                }else if(score < 50000){
                    yourName.setText(name + "   LV 8");
                }else if(score < 10000){
                    yourName.setText(name + "   LV 9");
                }else{
                    yourName.setText(name + "   LV MAX");
                }
            }
        });

        timer3.setRepeats(false);
//        複数回実行させないようにする

//                    ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー


//        タイマーのスタート
        timer1.start();

//        ーーーーーーーーーーーーーーーーーーーーー　クリックイベント　ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

        mercy.addActionListener(new ActionListener() {

            /**
                 * Invoked when an action occurs.
                 *
                 * @param e the event to be processed
                 */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(actionFlag == 0){
                    messageText.setFont(font);
                    messageText.setText("＊ あ、逃げるんすね笑\n 　オイラは構わないっすけど笑");
                    timer4 = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });
                    timer4.start();
                    actionFlag = 1;
                }
            }
        });
        item.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(actionFlag == 0) {
                        item.setForeground(orange);
//                    ボタンを押すと文字と枠をオレンジに戻す
                        item.setBorder(new LineBorder(orange, 5, false));
                        actionFlag = 1;
                        motionFlag = 2;
                        action();
//                    アクションを実行
                    }
                }
        });
        act.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(actionFlag == 0) {
                        act.setForeground(orange);
                        act.setBorder(new LineBorder(orange, 5, false));
                        actionFlag = 1;
                        motionFlag = 1;
                        action();
                    }
                }
        });
        fight.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(actionFlag == 0) {
                        fight.setForeground(orange);
                        fight.setBorder(new LineBorder(orange, 5, false));
                        actionFlag = 1;
                        motionFlag = 0;
                        action();
                    }
                }
        });

//        ーーーーーーーーーーーーーーーーーーーーーーー　マウスオーバーイベント　ーーーーーーーーーーーーーーーーーーーーーーーーーーー

        mercy.addMouseListener(new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(actionFlag == 0) {
                        mercy.setForeground(yellow);
//                        マウスオーバーした時にボタンの文字と枠を黄色にする
                        mercy.setBorder(new LineBorder(yellow, 5, false));
                    }
                }
        });
        item.addMouseListener(new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(actionFlag == 0) {
                        item.setForeground(yellow);
                        item.setBorder(new LineBorder(yellow, 5, false));
                    }
                }
        });
        act.addMouseListener(new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(actionFlag == 0) {
                        act.setForeground(yellow);
                        act.setBorder(new LineBorder(yellow, 5, false));
                    }
                }
        });
        fight.addMouseListener(new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(actionFlag == 0) {
                        fight.setForeground(yellow);
                        fight.setBorder(new LineBorder(yellow, 5, false));
                    }
                }
        });

//        ーーーーーーーーーーーーーーーーーーーーーー　マウスアウト（？）処理　ーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

        mercy.addMouseListener(new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseExited(MouseEvent e) {
                    if(actionFlag == 0) {
                        mercy.setForeground(orange);
                        mercy.setBorder(new LineBorder(orange, 5, false));
                    }
                }
        });
        item.addMouseListener(new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseExited(MouseEvent e) {
                    if(actionFlag == 0) {
                        item.setForeground(orange);
                        item.setBorder(new LineBorder(orange, 5, false));
                    }
                }
        });
        act.addMouseListener(new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseExited(MouseEvent e) {
                    if(actionFlag == 0) {
                        act.setForeground(orange);
                        act.setBorder(new LineBorder(orange, 5, false));
                    }
                }
        });
        fight.addMouseListener(new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseExited(MouseEvent e) {
                    if(actionFlag == 0) {
                        fight.setForeground(orange);
                        fight.setBorder(new LineBorder(orange, 5, false));
                    }
                }
        });

//        ーーーーーーーーーーーーーーーーーー　マウスの位置取得　ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

        field.addMouseMotionListener(new MouseMotionAdapter() {
        });
        battle.addMouseMotionListener(new MouseMotionAdapter() {
            /**
             * Invoked when the mouse button has been moved on a component
             * (with no buttons no down).
             *
             * @param e
             */
            @Override
            public void mouseMoved(MouseEvent e) {
                pi = MouseInfo.getPointerInfo();
                pt = pi.getLocation();
//                マウスの位置を取得
                mouseFlag = 1;
            }
        });
    }

//    ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

    public void action(){
        if(actionFlag == 1){
//            System.out.println("timer1");
            actionFlag = 2;
            timer2.start();
            if(motionFlag == 0){
                messageText.setText("＊　なんだろう・・・歯向かうの\n　 やめてもらっていいですか？");
            }else if(motionFlag == 1){
                Random r = new Random();
                int number = r.nextInt(2);
                if(number == 0){
                    messageText.setText("＊　西村◯之との賭けに負けた\n　 勝てるとおもったんすか？笑");
                    speed++;
                }else{
                    messageText.setText("＊　西村◯之との賭けに勝った\n　 結構強いっすね・・・");
                    score += 1000 + (speed * 500);
                }
            }else if(motionFlag == 2){
                messageText.setText("＊　舌を肥やすな　飯が不味くなる！\n");
                myPoint += 5;
                if(myPoint > 20){
                    myPoint = 20;
                }
                myHP.setText("HP   " + myPoint + " / 20");
            }
        }
    }

    public void hantei(){
        if(muteki_flag == 0 && !(pos_x_re > enemy1.getX() + 100 || pos_x_re + 23 < enemy1.getX() + 22 || pos_y_re > enemy1.getY() + 90 || pos_y_re + 10 < enemy1.getY())){
            myPoint = myPoint - 3;
            myHP.setText("HP   " + myPoint + " / 20");
            muteki_flag = 1;
            muteki_timer.start();
        }else if(muteki_flag == 0 && !(pos_x_re > enemy2.getX() + 100 || pos_x_re + 23 < enemy2.getX() + 22 || pos_y_re > enemy2.getY() + 90 || pos_y_re + 10 < enemy2.getY())){
            myPoint = myPoint - 3;
            myHP.setText("HP   " + myPoint + " / 20");
            muteki_flag = 1;
            muteki_timer.start();
        }else if(muteki_flag == 0 && !(pos_x_re > enemy3.getX() + 100 || pos_x_re + 23 < enemy3.getX() + 22 || pos_y_re > enemy3.getY() + 90 || pos_y_re + 10 < enemy3.getY())){
            myPoint = myPoint - 3;
            myHP.setText("HP   " + myPoint + " / 20");
            muteki_flag = 1;
            muteki_timer.start();
        }else if(muteki_flag == 0 && !(pos_x_re > enemy4.getX() + 100 || pos_x_re + 23 < enemy4.getX() + 22 || pos_y_re > enemy4.getY() + 90 || pos_y_re + 10 < enemy4.getY())){
            myPoint = myPoint - 3;
            myHP.setText("HP   " + myPoint + " / 20");
            muteki_flag = 1;
            muteki_timer.start();
        }

        if(myPoint <= 0){
            enemy1.setVisible(false);
            enemy2.setVisible(false);
            enemy3.setVisible(false);
            enemy4.setVisible(false);
            myImg.setVisible(false);
            gameOver();
        }

        move(enemy1);
        timer_e1 = new Timer(1000 - (speed * 50), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                e_count1 = 1;
            }
        });
        timer_e1.setRepeats(false);
        timer_e1.start();
        if(e_count1 == 1){
            enemy2.setVisible(true);
            move(enemy2);
        }

        timer_e2 = new Timer(2000 - (speed * 75), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                e_count2 = 1;
            }
        });
        timer_e2.setRepeats(false);
        timer_e2.start();
        if(e_count2 == 1){
            move(enemy3);
            enemy3.setVisible(true);
        }

        timer_e3 = new Timer(3000 - (speed * 100), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                e_count3 = 1;
            }
        });
        timer_e3.setRepeats(false);
        timer_e3.start();
        if(e_count3 == 1){
            move(enemy4);
            enemy4.setVisible(true);
        }
    }

    private JLabel move(JLabel enemy){
        int e_x = enemy.getX();
        int e_y = enemy.getY();
        Random rand = new Random();

        e_x -= 1 + speed;
        if(e_x < 330){
            e_x = 900;
            e_y = rand.nextInt(250) + 400;
        }
        enemy.setBounds(e_x,e_y,100,100);
        return enemy;
    }

    private void gameOver(){
        enemy1.setVisible(false);
        enemy2.setVisible(false);
        enemy3.setVisible(false);
        enemy4.setVisible(false);
        myImg.setVisible(false);
        timer1.stop();
        timer2.stop();
        timer3.stop();
        timer_e1.stop();
        timer_e2.stop();
        timer_e3.stop();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(Gameoverpanel);
        Gameoverpanel.setLayout(null);
        Gameoverpanel.setBackground(Color.black);
        JButton retry = new JButton();
        JButton stop = new JButton();

        BufferedImage end_img;
        try{
            end_img = ImageIO.read(this.getClass().getResource("img/gameover.png"));
            Image image = end_img;
            Image newImg = image.getScaledInstance(1000, 600,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon icon;
            icon = new ImageIcon(newImg);
            JLabel end = new JLabel(icon);
            end.setBounds(200,100,1000,600);
            Gameoverpanel.add(end);
        }catch (IOException e){
            System.out.println("画像読み込みエラー");
        }

        try{
            URL url = this.getClass().getResource("font/DeterminationJP.ttf");
            InputStream in = url.openStream();
            font = Font.createFont(Font.TRUETYPE_FONT,in).deriveFont(56f);
        }catch(FontFormatException e){
            System.out.println("形式がフォントではありません。");
        }catch(IOException e){
            System.out.println("入出力エラーでフォントを読み込むことができませんでした。");
        }catch (NullPointerException e){
            System.out.println("入出力エラーでフォントを読み込むことができませんでした。");
        }

        retry.setFont(font);
        retry.setBorder(new LineBorder(Color.white, 5, false));
        retry.setForeground(Color.white);
        retry.setBounds(800,700,300,150);
        retry.setText("リトライ");
        Gameoverpanel.add(retry);

        retry.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                retry.setForeground(yellow);
//                  マウスオーバーした時にボタンの文字と枠を黄色にする
                retry.setBorder(new LineBorder(yellow, 5, false));
            }
        });
        retry.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                retry.setForeground(Color.white);
                retry.setBorder(new LineBorder(Color.white, 5, false));
            }
        });
        retry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStart();
            }
        });

        stop.setFont(font);
        stop.setBorder(new LineBorder(Color.white, 5, false));
        stop.setForeground(Color.white);
        stop.setBounds(300,700,300,150);
        stop.setText("やめる");
        Gameoverpanel.add(stop);

        stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                stop.setForeground(yellow);
//                  マウスオーバーした時にボタンの文字と枠を黄色にする
                stop.setBorder(new LineBorder(yellow, 5, false));
            }
        });
        stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                stop.setForeground(Color.white);
                stop.setBorder(new LineBorder(Color.white, 5, false));
            }
        });
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    private Image getScaledImage(Image srcImg, int w, int h){
//    画像のサイズを変更する関数
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public static void main(String[] args) {
        // イベントキューに画面の起動スレッドを追加
        EventQueue.invokeLater(() -> {
            new lastissue().setVisible(true);
            lastissue a = new lastissue();
            a.setResizable(false);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            gd.setFullScreenWindow(a);
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
