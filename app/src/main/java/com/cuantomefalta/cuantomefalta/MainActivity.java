package com.cuantomefalta.cuantomefalta;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

//Libreria externa para la implementacion del tutorial inicial
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class MainActivity extends AppCompatActivity {

    //Declaracion de todas las variables utilizadas, se evito el uso de arrays para
    //asi darle mayor legibilidad al codigo acerca del proposito de cada variable
    int rows = 1;
    Context context;
    EditText weighing, weighing1, weighing2, weighing3, grade, grade1, grade2, grade3, goalGrade;
    TextView converted, converted1, converted2, converted3,first_text,second_text;
    TextSwitcher finalGrade;
    Animation fadeIn;
    Animation fadeOut, shake,shake2,appear,disappear,pop_in,pop_out;
    Settings settings;
    View shareView;
    ImageButton mShareFab, mAddFab,mDeleteFab,mResetFab, mRunFab;
    private AdView mAdView;
    private InterstitialAd mInterstitial;

    //Constante para establecer el numero de veces que el usuario puede hacer un calculo de notas
    // para que se le muestre un aviso intertitial
    private int final RUNS_TO_SHOW_INTER_AD=6;
    
    //Constante para establecer el numero de veces que el usuario puede iniciar la aplicacion
    // para que se le muestre un aviso de calificacion de la app
    private int final LAUNCHES_TO_SHOW_RATE_PROMPT=3;
    
    TourGuide mTutorialHandler;
    private Animation mEnterAnimation, mExitAnimation;

    SharedPreferences prefs = null;
    boolean mIsFirstRun;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        settings = new Settings();
        prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        activity = this;

        //Obtener la referencia de los campos de la interfaz
        getViewsReference();

        //cargar las animaciones utilizadas
        loadAnimations();

        //Metodo que se encarga de la solicitud y carga de los avisos del banner fijo
        setBannerAdStuff();

        //Verificar si la aplicacion esta iniciando por primera vez
        checkFirstRun();

        //Metodo para chequear el numero de veces que la aplicacion se ha iniciado.
        checkLaunchCount();

        //Establecer los listener de los botones fab de la interfaz
        setFabsListeners();

        //Establecer los listeners de los campos de ponderacion, ya que se da informacion inmediata al usuario indicanole si
        //su entreda fue correcta o no
        setWeighingListener();
        
        //Establecer los listeners de los campos de notas, ya que se da informacion inmediata al usuario indicanole si
        //su entreda fue correcta o no
        setGradeListeners();

        //Establecer los listeners de los campos de notas deseadas, ya que se da informacion inmediata al usuario indicanole si
        //su entreda fue correcta o no
        setGoalGradeListeners();




    }

    //Verificar si la aplicacion esta iniciando por primera vez
    private void checkFirstRun()
    {
        //Consulta en el archivo de preferencias si firstrun == true
        mIsFirstRun = prefs.getBoolean("firstrun", true);
        if (mIsFirstRun) {

            //Cambiar el valor a falso
            prefs.edit().putBoolean("firstrun", false).commit();
            
           //Crea y establece RunBtnCount a cero, esta preferencia se usa para conocer el numero de veces que 
           //el usuario realiza un calculo, ya que de el depende cuando se le mostrara el anuncio.
            prefs.edit().putInt("RunBtnCount", 0).commit();
            
            //Crea y establece LaunchCount a -1, esta preferencia se usa para conocer el numero de veces que 
            //el usuario realiza inicia la app, ya que de el depende cuando se le pedira que califique la app.
            prefs.edit().putInt("LaunchCount",-1).commit();
            
            //Se envia a la actividad Splash activity, la cual es la encargada de mostrar el logo inicial de la aplicacion.
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
    //Metodo para chequear el numero de veces que la aplicacion se ha iniciado.
    private void checkLaunchCount()
    {
        //Obtiene el valor de la preferencia LaunchCount y le suma una unidad.
        prefs.edit().putInt("LaunchCount", prefs.getInt("LaunchCount",0)+1).commit();
        
        //almacena el valor en una variable para efectos de decisiones.
        int  count =  prefs.getInt("LaunchCount",-1);
        
        //Si se ha cumplido el numero de inicios correspondientes
        if(count == LAUNCHES_TO_SHOW_RATE_PROMPT)
        {
            //Se crea un cuadro de dialogo solicitando al usuario que califique la aplicacion
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            //El titulo del cuadro y el mensaje que contendra
            builder.setMessage(R.string.rate_text)
                    .setTitle(R.string.rateUs);

            //Boton que dirige al usuario para que vaya a playstore a calificar
            builder.setPositiveButton(R.string.calificar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getPackageName())));
                }
            });
            
            //Boton para que el usuario decline la calificacion...si toca este boton no se le volvera a preguntar
            builder.setNegativeButton(R.string.noGracias, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            //Boton para recordarle de la calificacion en otro momento
            builder.setNeutralButton(R.string.luego, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //como el usuario pidio mas tiempo para calificar, el contador de lanzamientos se re inicializa a cero
                    prefs.edit().putInt("LaunchCount",0).commit();
                    dialog.dismiss();
                }
            });

            //creacion y exhibicion del dialogo
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    //Obtener las referencias de todos los campos de los cuales se extraera informacion.
    private void getViewsReference()
    {
        //getting reference to weighings Edittext fields
        weighing =  findViewById(R.id.weighing);
        weighing1 =  findViewById(R.id.weighing1);
        weighing2 =  findViewById(R.id.weighing2);
        weighing3 =  findViewById(R.id.weighing3);

        grade =  findViewById(R.id.grade);
        grade1 =  findViewById(R.id.grade1);
        grade2 =  findViewById(R.id.grade2);
        grade3 =  findViewById(R.id.grade3);

        converted =  findViewById(R.id.converted);
        converted1 =  findViewById(R.id.converted1);
        converted2 =  findViewById(R.id.converted2);
        converted3 =  findViewById(R.id.converted3);

        mAddFab =  findViewById(R.id.floatingActionButton8);
        mResetFab = findViewById(R.id.floatingActionButton10);
        mDeleteFab =  findViewById(R.id.floatingActionButton9);
        mRunFab =  findViewById(R.id.floatingActionButton11);
        mShareFab =  findViewById(R.id.floatingActionButton12);

        finalGrade=  findViewById(R.id.final_grade);
        goalGrade =  findViewById(R.id.goalGrade);
        first_text =  findViewById(R.id.first_text);
        second_text =  findViewById(R.id.second_text);
        shareView =  findViewById(R.id.shareView);


    }

    //Carga de todas las animaciones utilizadas en la aplicacion. (durante el proceso de carga y calculo de notas)
    private void loadAnimations()
    {
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        shake2 = AnimationUtils.loadAnimation(this, R.anim.shake2);
        appear = AnimationUtils.loadAnimation(this,R.anim.appear);
        disappear = AnimationUtils.loadAnimation(this,R.anim.disappear);
        pop_in = AnimationUtils.loadAnimation(this,R.anim.pop_in);
        pop_out = AnimationUtils.loadAnimation(this,R.anim.pop_out);

        finalGrade.setInAnimation(context, android.R.anim.slide_in_left);
        finalGrade.setOutAnimation(context, android.R.anim.slide_out_right);

        mEnterAnimation = new AlphaAnimation(0f, 1f);
        mEnterAnimation.setDuration(600);
        mEnterAnimation.setFillAfter(true);

        mExitAnimation = new AlphaAnimation(1f, 0f);
        mExitAnimation.setDuration(600);
        mExitAnimation.setFillAfter(true);
    }

    //Listener de los botones flotantes
    private void setFabsListeners()
    {
        //Este boton añade filas de notas a ingresar.
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (weighing1.getVisibility() == View.GONE) {
                    makeVisible(weighing1, grade1, converted1);
                    rows++;
                } else {
                    if (weighing2.getVisibility() == View.GONE) {
                        makeVisible(weighing2, grade2, converted2);
                        rows++;
                    } else {
                        if (weighing3.getVisibility() == View.GONE) {
                            makeVisible(weighing3, grade3, converted3);
                            rows++;
                        }
                    }
                }

            }
        });

        //Este boton pone a default del numer de filas
        mResetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (weighing3.getVisibility() == View.VISIBLE) {
                    makeGone(weighing3, grade3, converted3);
                    rows--;
                }

                if (weighing2.getVisibility() == View.VISIBLE) {
                    makeGone(weighing2, grade2, converted2);
                    rows--;
                }

                if (weighing1.getVisibility() == View.VISIBLE) {
                    makeGone(weighing1, grade1, converted1);
                    rows--;
                }

                //Ocultamiento de filas extras
                weighing.setText("");
                grade.setText("");
                converted.setText("1.0");

                finalGrade.setText("");
                if(mShareFab.getVisibility()==View.VISIBLE)
                {
                    mShareFab.setVisibility(View.GONE);
                    mShareFab.startAnimation(pop_out);
                }
                if(first_text.getVisibility()==View.VISIBLE) {
                    first_text.setVisibility(View.GONE);
                    first_text.startAnimation(disappear);
                }

                if(second_text.getVisibility()==View.VISIBLE) {
                    second_text.setVisibility(View.GONE);
                    second_text.startAnimation(disappear);
                }

                goalGrade.setText(R.string.five);
                if(goalGrade.getVisibility()==View.GONE) {
                    goalGrade.setVisibility(View.VISIBLE);
                    goalGrade.startAnimation(appear);
                }
                //Ocultamiento de filas extras
            }
        });

        //Boton que borra toda la informacion ingresada e inicializa a valores default las filas activas
        mDeleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Setteo de todos los valores de las filas activas a sus valores default
                finalGrade.setText("");
                if(first_text.getVisibility()==View.VISIBLE)
                {
                    first_text.setVisibility(View.GONE);
                    first_text.startAnimation(disappear);
                    mShareFab.setVisibility(View.GONE);
                    mShareFab.startAnimation(pop_out);
                }

                if(second_text.getVisibility()==View.VISIBLE)
                {
                    second_text.setVisibility(View.GONE);
                    second_text.startAnimation(disappear);
                }

                if (weighing3.getVisibility() == View.VISIBLE) {
                    makeGone(weighing3, grade3, converted3);
                    rows--;
                } else {
                    if (weighing2.getVisibility() == View.VISIBLE) {
                        makeGone(weighing2, grade2, converted2);
                        rows--;
                    } else {
                        if (weighing1.getVisibility() == View.VISIBLE) {
                            makeGone(weighing1, grade1, converted1);
                            rows--;
                        }
                    }
                }

            }
        });

        //Boton para compartir el resultado del calculo en las redes sociales.
        mShareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   takeScreenShot(shareView);

            }
        });

        //Boton para realizar el calculo de la nota
        mRunFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean itDidCalculateSoCount = false;
                //Verificar si todos los campos activos tiene valores validos
                if (!areAllFieldsSet()) {
                    //Verifica que la suma de las ponderaciones sea 100%
                    if (sumWeighings() == 100) {

                        //Activa el boton de compartir el resultado
                        if(mShareFab.getVisibility()==View.GONE)
                        {
                            mShareFab.setVisibility(View.VISIBLE);
                            mShareFab.startAnimation(pop_in);
                        }
                        //Metodo principal para el calculo de la nota definitiva
                        calculateDefinitiveGrade();
                        
                        itDidCalculateSoCount = true;

                    } else {
                        //Si la ponderaciones son superiores a 100 hay un error en los datos ingresados
                        if (sumWeighings() > 100) {


                            finalGrade.setText("");
                            //oculta el boton de compartir en caso de que estuviera activ
                            if(mShareFab.getVisibility()==View.VISIBLE)
                            {
                                mShareFab.setVisibility(View.GONE);
                                mShareFab.startAnimation(pop_out);
                            }
                            //Oculta y setea a default los valores que depennden de una correcta entrada de datos
                            if(goalGrade.getVisibility()==View.VISIBLE)
                            {
                                first_text.setVisibility(View.GONE);
                                second_text.setVisibility(View.GONE);
                                goalGrade.setVisibility(View.GONE);
                                first_text.startAnimation(disappear);
                                second_text.startAnimation(disappear);
                                goalGrade.startAnimation(disappear);
                            }
                            Toast.makeText(context, "Las ponderaciones no pueden sumar mas de 100%", Toast.LENGTH_SHORT).show();

                        } else {

                            //Si no son cuatro filas de notas, el usuario desea calcular cuanto le falta en el ultimo examen
                            if(rows!=4) {
                                calculateHowManyPoints();
                                itDidCalculateSoCount = true;

                            }
                            else
                            {
                                //Validacion de otro error en las ponderaciones
                                finalGrade.setText("");
                                if(mShareFab.getVisibility()==View.VISIBLE)
                                {
                                    mShareFab.setVisibility(View.GONE);
                                    mShareFab.startAnimation(pop_out);
                                }
                                if(goalGrade.getVisibility()==View.VISIBLE)
                                {
                                    first_text.setVisibility(View.GONE);
                                    second_text.setVisibility(View.GONE);
                                    goalGrade.setVisibility(View.GONE);
                                    first_text.startAnimation(disappear);
                                    second_text.startAnimation(disappear);
                                    goalGrade.startAnimation(disappear);
                                }
                                Toast.makeText(context, "Las ponderaciones deben sumar 100%", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                }

                //si logro realizar el calculo
                if(itDidCalculateSoCount)
                {
                    //Actualiza el archivo de preferencias para aumentar en 1 el numero de veces que el usuario a calculado satisfactoriamente
                    prefs.edit().putInt("RunBtnCount",prefs.getInt("RunBtnCount",0)+1).commit();

                    //obtiene el valor de la preferencia luego de la actualizacion y verifica si alcanzo el limite para mostrar el anuncio 
                    //interstitial
                    int count = prefs.getInt("RunBtnCount",0);
                    if(count >= RUNS_TO_SHOW_INTER_AD)
                    {
                        setInterstitialAdStuff();

                    }
                }


            }
        });

    }

    //Listener para los ccampos de ponderacion
    private void setWeighingListener()
    {
        weighing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weighing.setText("");

            }
        });

        //Validacion de cambio de foco
        weighing.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    arrangeText(weighing);

                } else {
                    weighing.setText("");

                }

            }
        });

        //manejo de la entrada por teclado
        weighing.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    arrangeText(weighing);

                }
                return false;
            }
        });

        weighing1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weighing1.setText("");

            }
        });

        weighing1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    arrangeText(weighing1);

                } else {
                    weighing1.setText("");

                }
            }
        });

        weighing1.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    arrangeText(weighing1);
                }
                return false;
            }
        });

        weighing2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weighing2.setText("");
            }
        });

        weighing2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    arrangeText(weighing2);

                } else {
                    weighing2.setText("");
                }
            }
        });

        weighing2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    arrangeText(weighing2);
                }
                return false;
            }
        });

        weighing3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weighing3.setText("");
            }
        });

        weighing3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    arrangeText(weighing3);

                } else {
                    weighing3.setText("");
                }
            }
        });

        weighing3.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    arrangeText(weighing3);
                }
                return false;
            }
        });

    }

    //Listeners para los campos de Notas
    private void setGradeListeners()
    {
        grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grade.setText("");
                converted.setText("1.0");

            }
        });

        //Validacion para cambio de foco
        grade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focusEvent(hasFocus, grade, converted);
            }
        });

        //Listener del enter del editor de teclado
        grade.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                editorEvent(actionId, event, grade, converted);

                return false;
            }
        });

        grade1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grade1.setText("");
                converted1.setText("1.0");
            }
        });

        grade1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focusEvent(hasFocus, grade1, converted1);

            }
        });

        grade1.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                editorEvent(actionId, event, grade1, converted1);
                return false;
            }
        });

        grade2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grade2.setText("");
                converted2.setText("1.0");
            }
        });

        grade2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focusEvent(hasFocus, grade2, converted2);

            }
        });

        grade2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                editorEvent(actionId, event, grade2, converted2);
                return false;
            }
        });

        grade3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grade3.setText("");
                converted3.setText("1.0");
            }
        });

        grade3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focusEvent(hasFocus, grade3, converted3);

            }
        });

        grade3.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                editorEvent(actionId, event, grade3, converted3);
                return false;
            }
        });

    }

    //Listener del campo de nota deseada
    private void setGoalGradeListeners()
    {
        goalGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalGrade.setText("");

            }
        });

        goalGrade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!validateGoalGrade(goalGrade))
                    {
                        goalGrade.setText("");
                        if(!goalGrade.getText().toString().isEmpty())
                            goalGrade.startAnimation(shake2);
                    }

                } else {

                    goalGrade.setText("");

                }

            }
        });

        goalGrade.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if(!validateGoalGrade(goalGrade))
                    {
                        goalGrade.setText("");
                        if(!goalGrade.getText().toString().isEmpty())
                            goalGrade.startAnimation(shake2);

                    }
                }
                return false;
            }
        });

    }

    //Manejo de procesos del anuncio Banner
    private void setBannerAdStuff()
    {
        //Inicializacion con Id del publicador
        MobileAds.initialize(this, "ca-app-pub-2505831397151341~3934790750");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitial = new InterstitialAd(context);
        mInterstitial.setAdUnitId("ca-app-pub-2505831397151341/2565868761");
        AdRequest interRequest = new AdRequest.Builder().build();
        mInterstitial.loadAd(interRequest);

        mInterstitial.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {

                mInterstitial.loadAd(new AdRequest.Builder().build());
            }

        });

    }

    //Manejo de procesos del anuncio Interstitial
    private void setInterstitialAdStuff()
    {
        if(mInterstitial.isLoaded()) {
            Toast.makeText(context,"En breve un anuncio publicitario",Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                public void run() {

                    try {
                        Thread.sleep(3000);

                    } catch (InterruptedException ex) {

                    }
                    mAdView.post(new Runnable() {
                        @Override
                        public void run() {


                            mInterstitial.show();
                            prefs.edit().putInt("RunBtnCount", 0).commit();


                        }
                    });

                }
            }).start();
        }

        //Al mostrar el anuncion se re inicializa el contador
        else
        {
            prefs.edit().putInt("RunBtnCount",RUNS_TO_SHOW_INTER_AD-1).commit();
        }

    }

    //Validacion del valor ingresado
    public boolean validate(Editable text)
    {
        String info = text.toString().split("%")[0];

        try {
            if (Integer.parseInt(info) <= 0 || Integer.parseInt(info) > 100) {
                //El ponderado de un examen debe ser un numero entre 1 y 100
                Toast.makeText(context, "El ponderado de un examen debe ser un numero entre 1 y 100", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (NumberFormatException nfe) {
            //Este campo solo acepta valores numericos enteros
            Toast.makeText(context, "Este campo solo acepta valores numericos enteros", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    //Validacion del valor ingresado ne el campo de nota
    public boolean validateGrade(Editable text)
    {
        String info = text.toString();
        if (!info.isEmpty()) {
            try {
                if (Integer.parseInt(info) < settings.getMin_grade() || Integer.parseInt(info) > settings.getMax_grade()) {
                    //La nota del examen debe estar en la escala escogida de min-max
                    Toast.makeText(context, "La nota del examen debe estar en la escala escogida de " + settings.getMin_grade() + " a " + settings.getMax_grade(), Toast.LENGTH_LONG).show();

                    return false;
                }
            } catch (NumberFormatException nfe) {
                //Este campo solo acepta valores numericos enteros
                Toast.makeText(context, "Este campo solo acepta valores numericos enteros", Toast.LENGTH_LONG).show();
                return false;
            }

        } else return false;

        return true;
    }

    public void arrangeText(EditText et)
    {
        if (!et.getText().toString().isEmpty() && !et.getText().toString().contains("%")) {
            et.setText(et.getText() + "%");
            if (!validate(et.getText())) {
                if (!et.getText().toString().isEmpty())
                    et.startAnimation(shake);
                et.setText("");
            }
        }

    }

    //Metodo para hacer visible una fila
    public void makeVisible(EditText w, EditText g, TextView c)
    {

        w.setVisibility(View.VISIBLE);
        g.setVisibility(View.VISIBLE);
        c.setVisibility(View.VISIBLE);
        c.startAnimation(fadeIn);
        g.startAnimation(fadeIn);
        w.startAnimation(fadeIn);

    }

    //Metodo para ocultar una fila
    public void makeGone(EditText w, EditText g, TextView c)
    {

        w.setVisibility(View.GONE);
        g.setVisibility(View.GONE);
        c.setVisibility(View.GONE);
        g.setText("");
        w.setText("");
        c.setText("1.0");
        w.startAnimation(fadeOut);
        c.startAnimation(fadeOut);
        g.startAnimation(fadeOut);



    }

    //SSuma de las ponderaciones
    public int sumWeighings()
    {
        int sum = 0;

        if (weighing.getVisibility() == View.VISIBLE) {
            sum += Integer.parseInt(weighing.getText().toString().split("%")[0]);
        }

        if (weighing1.getVisibility() == View.VISIBLE) {
            sum += Integer.parseInt(weighing1.getText().toString().split("%")[0]);
        }

        if (weighing2.getVisibility() == View.VISIBLE) {
            sum += Integer.parseInt(weighing2.getText().toString().split("%")[0]);
        }

        if (weighing3.getVisibility() == View.VISIBLE) {
            sum += Integer.parseInt(weighing3.getText().toString().split("%")[0]);
        }

        return sum;
    }

    //Verificacion de que todos los campos activos tengan datos ingresados
    public boolean areAllFieldsSet()
    {
        boolean flag = false;
        if (weighing.getText().toString().isEmpty()) {
            weighing.startAnimation(shake);
            flag = true;

        }

        if (weighing1.getText().toString().isEmpty() && weighing1.getVisibility() == View.VISIBLE) {
            weighing1.startAnimation(shake);
            flag = true;

        }

        if (weighing2.getText().toString().isEmpty() && weighing2.getVisibility() == View.VISIBLE) {
            weighing2.startAnimation(shake);
            flag = true;

        }

        if (weighing3.getText().toString().isEmpty() && weighing3.getVisibility() == View.VISIBLE) {
            weighing3.startAnimation(shake);
            flag = true;

        }

        if (grade.getText().toString().isEmpty()) {
            grade.startAnimation(shake);
            flag = true;

        }

        if (grade1.getText().toString().isEmpty() && grade1.getVisibility() == View.VISIBLE) {
            grade1.startAnimation(shake);
            flag = true;

        }

        if (grade2.getText().toString().isEmpty() && grade2.getVisibility() == View.VISIBLE) {
            grade2.startAnimation(shake);
            flag = true;

        }

        if (grade3.getText().toString().isEmpty() && grade3.getVisibility() == View.VISIBLE) {
            grade3.startAnimation(shake);
            flag = true;

        }

        if (goalGrade.getText().toString().isEmpty()) {
            goalGrade.startAnimation(shake2);
            flag = true;

        }

        if (flag) {
            Toast.makeText(context, "Debes llenar los campos que faltan", Toast.LENGTH_LONG).show();
            if(first_text.getVisibility()==View.VISIBLE) {
                first_text.setVisibility(View.GONE);
                second_text.setVisibility(View.GONE);
                first_text.startAnimation(disappear);
                second_text.startAnimation(disappear);
                finalGrade.setText("");
                if(mShareFab.getVisibility()==View.VISIBLE)
                {
                    mShareFab.setVisibility(View.GONE);
                    mShareFab.startAnimation(pop_out);
                }
            }
        }

        return flag;
    }

    //Metodo para llamar la atencion del usuario para que termine de llenar el campo al cambiar de foco
    public void focusEvent(boolean hf, EditText g, TextView c)
    {
        if (!hf) {

            validateAndSet(g, c);

        } else {
            g.setText("");
            c.setText("1.0");
            //Toast.makeText(c,"click",Toast.LENGTH_SHORT).show();
        }

    }

     //Metodo para llamar la atencion del usuario para que termine de llenar el campo al abandonar el editor de texto
    public void editorEvent(int actionId, KeyEvent event, EditText g, TextView c)
    {
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

            validateAndSet(g, c);
        }

    }

    public void validateAndSet(EditText g, TextView c)
    {
        if (!validateGrade(g.getText())) {
            if (!g.getText().toString().isEmpty())
                g.startAnimation(shake);
            g.setText("");
            c.setText("1.0");

        } else {
            c.setText(settings.getConversionOf(Integer.parseInt(g.getText().toString())).toString());
        }

    }

    //Metodo para calcular cuantos puntos le hacen falta al usiario para pasar una materia
    public void calculateHowManyPoints()
    {


        double weighingLeft = 100 - sumWeighings();
        weighingLeft /= 100;

        double w = (Double.parseDouble(weighing.getText().toString().split("%")[0])) / 100;
        double wgrade = w * Double.parseDouble(converted.getText().toString());
        double w1, w2, w3;

        if (weighing1.getVisibility() == View.VISIBLE) {
            w1 = Double.parseDouble(weighing1.getText().toString().split("%")[0]) / 100;
            wgrade += w1 * Double.parseDouble(converted1.getText().toString());
        }

        if (weighing2.getVisibility() == View.VISIBLE) {
            w2 = Double.parseDouble(weighing2.getText().toString().split("%")[0]) / 100;
            wgrade += w2 * Double.parseDouble(converted2.getText().toString());
        }

        if (weighing3.getVisibility() == View.VISIBLE) {
            w3 = Double.parseDouble(weighing3.getText().toString().split("%")[0]) / 100;
            wgrade += w3 * Double.parseDouble(converted3.getText().toString());
        }

        double true_grade= Double.parseDouble(goalGrade.getText().toString()) - 0.50;
        double result = true_grade - wgrade;
        double goal = round(result/weighingLeft,6);

        if(goal>9)
        {
            finalGrade.setText("F/E");
            if(mShareFab.getVisibility()==View.VISIBLE)
            {
                mShareFab.setVisibility(View.GONE);
                mShareFab.startAnimation(pop_out);
            }
            if(first_text.getVisibility()==View.VISIBLE) {
                first_text.setVisibility(View.GONE);
                second_text.setVisibility(View.GONE);
                first_text.startAnimation(disappear);
                second_text.startAnimation(disappear);
            }
            Toast.makeText(context, "Estas bajo escala para llegar a "+goalGrade.getText()+" puntos", Toast.LENGTH_SHORT).show();
        }

        else {

            if(wgrade>=true_grade)
            {
                if(first_text.getVisibility()==View.VISIBLE) {
                    first_text.setVisibility(View.GONE);
                    second_text.setVisibility(View.GONE);
                    first_text.startAnimation(disappear);
                    second_text.startAnimation(disappear);
                    finalGrade.setText("");

                }

                if(mShareFab.getVisibility()==View.VISIBLE)
                {
                    mShareFab.setVisibility(View.GONE);
                    mShareFab.startAnimation(pop_out);
                }
                Toast.makeText(context, "Ya alcanzas "+goalGrade.getText()+" puntos con las notas que obtuviste", Toast.LENGTH_LONG).show();
            }

            else {

                int true_goal = 1;
                while (true) {
                    if (settings.getConversionOf(true_goal) * weighingLeft + wgrade >= true_grade) {
                        break;
                    }
                    true_goal += 1;
                }

                finalGrade.setText("" + true_goal);
                first_text.setText("Me faltan");
                goalGrade.setVisibility(View.VISIBLE);
                first_text.setVisibility(View.VISIBLE);
                second_text.setVisibility(View.VISIBLE);
                goalGrade.startAnimation(appear);
                first_text.startAnimation(appear);
                second_text.startAnimation(appear);

                if(mShareFab.getVisibility()==View.GONE)
                {
                    mShareFab.setVisibility(View.VISIBLE);
                    mShareFab.startAnimation(pop_in);
                }


            }

        }



    }

    //Redondeo de decimales
    public double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.DOWN);
        return bd.doubleValue();
    }

    //Metodo para validar el campo de nota deseada
    public boolean validateGoalGrade(EditText gg)
    {
        String info = gg.getText().toString();

        if (!info.isEmpty()) {
            try {
                if (Integer.parseInt(info) <= 1 || Integer.parseInt(info) > 9) {

                    Toast.makeText(context, "La nota objetivo debe estar entre 2 y 9", Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (NumberFormatException nfe) {
                //Este campo solo acepta valores numericos enteros
                Toast.makeText(context, "Este campo solo acepta valores numericos enteros", Toast.LENGTH_LONG).show();
                return false;
            }
        }else return false;

        return true;

    }

    //Metodo para el calculo de la nota definitiva
    public void calculateDefinitiveGrade()
    {

        double w = (Double.parseDouble(weighing.getText().toString().split("%")[0])) / 100;
        double wgrade = w * Double.parseDouble(converted.getText().toString());
        double w1, w2, w3;

        if (weighing1.getVisibility() == View.VISIBLE) {
            w1 = Double.parseDouble(weighing1.getText().toString().split("%")[0]) / 100;
            wgrade += w1 * Double.parseDouble(converted1.getText().toString());
        }

        if (weighing2.getVisibility() == View.VISIBLE) {
            w2 = Double.parseDouble(weighing2.getText().toString().split("%")[0]) / 100;
            wgrade += w2 * Double.parseDouble(converted2.getText().toString());
        }

        if (weighing3.getVisibility() == View.VISIBLE) {
            w3 = Double.parseDouble(weighing3.getText().toString().split("%")[0]) / 100;
            wgrade += w3 * Double.parseDouble(converted3.getText().toString());
        }
        wgrade=round(wgrade,2);
        finalGrade.setText(""+wgrade);
        first_text.setText("Nota final");
        first_text.setVisibility(View.VISIBLE);
        first_text.startAnimation(appear);

        if(second_text.getVisibility()==View.VISIBLE)
        {
            second_text.setVisibility(View.GONE);
            second_text.startAnimation(disappear);
        }

        if(goalGrade.getVisibility()==View.VISIBLE) {
            goalGrade.setVisibility(View.GONE);
            goalGrade.startAnimation(disappear);

        }


    }
//Metodo para tomar una captura de pantalla de la nota requierida y compartirla en redes sociales
    public void takeScreenShot(View v)
    {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        try {

            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // override this image every time
            b.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.cuantomefalta.cuantomefalta.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }


    }

}







