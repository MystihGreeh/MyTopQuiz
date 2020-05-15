package MystihGreeh.TopQuiz.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import MystihGreeh.TopQuiz.Model.Question;
import MystihGreeh.TopQuiz.Model.QuestionBank;
import MystihGreeh.TopQuiz.R;


public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    // Create question and 4 answer buttons
    private TextView mQuestionTextView;
    private Button mAnswer1Button;
    private Button mAnswer2Button;
    private Button mAnswer3Button;
    private Button mAnswer4Button;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;

    private int mScore;
    private int mNumberOfQuestions;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "currentScore";
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";

    private boolean mEnableTouchEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        System.out.println("GameActivity::onCreate()");

        mQuestionBank = this.generateQuestions();

        if (savedInstanceState != null){
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mScore = 0;
            mNumberOfQuestions = 10;
        }

        mEnableTouchEvents = true;


        //Wire widgets
        mQuestionTextView = findViewById(R.id.activity_game1_question1_text);
        mAnswer1Button = findViewById(R.id.activity_game1_answer1_btn);
        mAnswer2Button = findViewById(R.id.activity_game1_answer2_btn);
        mAnswer3Button = findViewById(R.id.activity_game1_answer3_btn);
        mAnswer4Button = findViewById(R.id.activity_game1_answer4_btn);

        //Use the tag property to "name" the buttons
        mAnswer1Button.setTag(0);
        mAnswer2Button.setTag(1);
        mAnswer3Button.setTag(2);
        mAnswer4Button.setTag(3);

        mAnswer1Button.setOnClickListener(this);
        mAnswer2Button.setOnClickListener(this);
        mAnswer3Button.setOnClickListener(this);
        mAnswer4Button.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
            this.displayQuestion(mCurrentQuestion);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mNumberOfQuestions);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int responseIndex = (int) v.getTag();

        if (responseIndex == mCurrentQuestion.getAnswerIndex()) {
            // Good answer
            Toast.makeText(this, "Correct !", Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            //Wrong answer
            Toast.makeText(this, "Perdu", Toast.LENGTH_SHORT).show();
        }
        mEnableTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;

                //If this is the last question, ends the game.
                //Else, display the next question.
                if (--mNumberOfQuestions == 0){
                    //End game
                    endGame();
                } else {
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2000); //LENGTH_SHORT is usually 2 second long
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    private void endGame(){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);

       builder.setTitle("Bien joué !")
               .setMessage("Ton score est de "+mScore)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //End the activity
                       Intent intent = new Intent();
                       intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                       setResult(RESULT_OK, intent);
                       finish();
                   }
               })
               .setCancelable(false)
               .create()
               .show();
    }

    private void displayQuestion(final Question question){
        mQuestionTextView.setText(question.getQuestion());
        mAnswer1Button.setText(question.getChoiceList().get(0));
        mAnswer2Button.setText(question.getChoiceList().get(1));
        mAnswer3Button.setText(question.getChoiceList().get(2));
        mAnswer4Button.setText(question.getChoiceList().get(3));
    }

    private QuestionBank generateQuestions(){
        Question question1 = new Question("Parmi les jeux suivants, lequel n’a pas été adapté à partir d’un livre ?",
                            Arrays.asList("The Witcher", "Resident Evil", "Shadowrun", "Assassin's Creed"), 1);

        Question question2 = new Question("Le jeu de combat Tekken est sorti en premier :",
                Arrays.asList("Sur borne d'arcade", "Sur NES", "Sur Playstation", "Sur PC"), 0);

        Question question3 = new Question("Dans quel jeu le personnage de Mario a-t-il été développé en premier :",
                Arrays.asList("Super Mario", "Super Mario Bros", "Donkey Kong", "Super Mario 64"), 2);

        Question question4 = new Question("L’idée de base de The Last of Us a été développée après que ses créateurs aient visionné…",
                Arrays.asList("Un documentaire planète Terre sur les fourmis", "Un documentaire apocalypse sur la guerre", "Un documentaire sur l'impact écologique de l'Homme", "Un documentaire sur les virus et autres maladies"), 0);

        Question question5 = new Question("Red Dead Redemption fait suite à quel jeu ?",
                Arrays.asList("Red Dead Revolver", "Red Dead Imagination", "Red Dead Colt", "Red Dead Assassination"), 0);

        Question question6 = new Question("Quelle est la planète natale de Ratchet, dans \"Ratchet & Clank\" ?",
                Arrays.asList("Novalis", "Veldin", "Metropolis", "Arolis"), 1);

        Question question7 = new Question("Quel personnage n'est pas présent dans \"Tekken\" ?",
                Arrays.asList("Shirota", "Anna Williams", "Paul Phoenix", "Yoshimitsu"), 0);

        Question question8 = new Question("Quel est l’année de sortie de la PlayStation 3, en Europe ?",
                Arrays.asList("2005", "2006", "2007", "2008"), 2);

        Question question9 = new Question("Combien de fantômes y a-t-il dans \"PacMan\" ?",
                Arrays.asList("5", "2", "6", "4"), 3);

        Question question10 = new Question("De quelle nationalité est le créateur de \"Tétris\" ?",
                Arrays.asList("Français", "Japonais", "Américain", "Russe"), 3);

        Question question11 = new Question("En quelle année apparaît pour la première fois \"Donkey Kong\" ?",
                Arrays.asList("1978", "1980", "1981", "1983"), 2);

        Question question12 = new Question("Qui est le boss final de Street Fighter 2 ? (nom VF)",
                Arrays.asList("M. Bison", "Vega", "Akuma", "Dhalsim"), 0);

        Question question13 = new Question("Lequel de ces personnages n'est PAS un méchant de l'univers Final Fantasy ?",
                Arrays.asList("Ultimécia", "Palmécia", "Oméria", "Ardyn"), 2);

        Question question14 = new Question("Combien Bowser a-t-il d'enfant(s) ?",
                Arrays.asList("1", "4", "6", "8"), 3);

        Question question15 = new Question("Samus Aran (Metroid) est souvent opposé à un célèbre pirate de l'espace. Qui est-il ?",
                Arrays.asList("Vamp", "Ridley", "Nemesis", "Mother Brain"), 1);

        Question question16 = new Question("Qui est le boss de fin de Kingdom Hearts 2 ?",
                Arrays.asList("Kairi", "Jafar", "Nemesis", "Médusa"), 1);

        Question question17 = new Question("Duke Nukem, le héros le plus badass de l'univers, affronte...",
                Arrays.asList("Les loupaguns", "Les aliens de la planète Zorg", "Le général Graves", "Les Porcoflics"), 3);

        Question question18 = new Question("Qui est le boss de fin de Portal ?",
                Arrays.asList("Glados", "Mother Brain", "Wheatley", "Oméria"), 0);

        Question question19 = new Question("Altair et Ezio se battent contre un ordre religieux/militaire. Mais lequel ?",
                Arrays.asList("Les Templiers", "Les Hospitaliers", "Les Teutoniques", "Les Nazis"), 0);

        Question question20 = new Question("Quels aliens le Master Chief affronte-t-il dans la série des Halo ?",
                Arrays.asList("Yuuzhan Vong", "Xenomorphe", "Covenant", "HeadCrab"), 2);

        Question question21 = new Question("En quelle année le premier jeu vidéo a-t-il été créé ?",
                Arrays.asList("1981", "1978", "1969", "1962"), 3);

        Question question22 = new Question("Quelle année voit l'invention de la \"Game Boy\" ?",
                Arrays.asList("1969", "1979", "1989", "1999"), 2);

        Question question23 = new Question("Quand voyez-vous sortir la DS en 2D ?",
                Arrays.asList("2005", "2007", "2009", "2010"), 0);

        Question question24 = new Question("Dans God Of War, de quel dieu Kratos était-il le serviteur avant de se retourner contre lui ?",
                Arrays.asList("Anubis", "Arès", "Poséidon", "Hadès"), 1);

        Question question25 = new Question("Lequel de ces personnages de la série Soul Calibur est apparu pour la première fois dans Soul Calibur 2 ?",
                Arrays.asList("Raphael", "Kilik", "Nightmare", "Sophitia"), 0);

        Question question26 = new Question("De quelle célèbre série d’infiltration Sam Fisher est-il le héros ?",
                Arrays.asList("Metal Gear", "Splinter Cell", "Tenchu", "Hitman"), 1);

        Question question27 = new Question("Dans la série Devil May Cry, comment Dante a t’il baptisé ses pistolets ?",
                Arrays.asList("Black & White", "Agni & Rudra", "Dante & Vergil", "Ebony & Ivory"), 3);

        Question question28 = new Question("Dans Shadow Of The Colossus, comment se nomme le cheval du héros ?",
                Arrays.asList("Epona", "Wanda", "Agro", "Tornado"), 2);

        Question question29 = new Question("Quelle est la profession de Gordon Freeman, le héros de Half-Life ?",
                Arrays.asList("Policier", "Espion", "Marine", "Scientifique"), 3);

        Question question30 = new Question("Comment se nomme le troisième volet de la série The Elder Scrolls ?",
                Arrays.asList("Morrowind", "Daggerfall", "Arena", "Oblivion"), 0);

        return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5, question6, question7, question8, question9, question10, question11,
        question12, question13, question14, question15, question16, question17, question18, question19, question20, question21, question22, question23, question24, question25,
        question26,question27, question28, question29, question30));
    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("GameActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("GameActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        System.out.println("GameActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        System.out.println("GameActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.out.println("GameActivity::onDestroy()");
    }
}
