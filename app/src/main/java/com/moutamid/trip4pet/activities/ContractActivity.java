package com.moutamid.trip4pet.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityContractBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ContractActivity extends AppCompatActivity {
    ActivityContractBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityContractBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.toolbar.title.setText(R.string.terms_of_services);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        String contract = "Contratto di Utilizzo dei Dati e Informativa sulla Privacy per Trip4Pet\n" +
                "Data di entrata in vigore: 11/11/2024\n" +
                "1. Premessa\n" +
                "Questo Contratto disciplina i termini e le condizioni per il download, l'installazione e l'utilizzo dell'app Trip4Pet, nonché i diritti, obblighi e responsabilità degli utenti. Utilizzando l'app, l'utente accetta i termini del presente Contratto. Se non si è d'accordo con i termini, si prega di non utilizzare l'app.\n" +
                "2. Definizioni\n" +
                "•\t\"Trip4Pet\": Applicazione per viaggiatori con animali, sviluppata per condividere esperienze e recensioni su strutture e luoghi pet-friendly.\n" +
                "•\t\"Utente\": La persona fisica che scarica e utilizza l'app Trip4Pet.\n" +
                "•\t\"Dati Personali\": Nome, e-mail, posizione geografica, informazioni sull'animale posseduto (specie e taglia) e modalità di pagamento.\n" +
                "•\t\"Servizi\": Le funzionalità messe a disposizione dell'utente per condividere recensioni, esperienze e accedere ai contenuti premium dell'app.\n" +
                "3. Concessione in Licenza\n" +
                "Trip4Pet concede all'Utente una licenza limitata, non esclusiva, personale e revocabile per l'utilizzo dell'app e dei suoi servizi. La licenza è concessa esclusivamente per scopi personali e non commerciali.\n" +
                "4. Dati Raccolti e Modalità d'Uso\n" +
                "Trip4Pet raccoglie le seguenti informazioni personali:\n" +
                "•\tNome e indirizzo email;\n" +
                "•\tPosizione geografica per migliorare l’esperienza di navigazione e suggerire contenuti pertinenti;\n" +
                "•\tInformazioni sull’animale posseduto (specie e taglia);\n" +
                "•\tModalità di pagamento per l'accesso alle funzioni a pagamento dell'app.\n" +
                "5. Finalità del Trattamento dei Dati\n" +
                "I dati personali degli utenti sono utilizzati per:\n" +
                "•\tMigliorare il servizio e personalizzare l’esperienza utente;\n" +
                "•\tGestire le recensioni, i luoghi e le esperienze condivise;\n" +
                "•\tConsentire l'accesso alle funzionalità a pagamento e gestire le transazioni.\n" +
                "In futuro, i dati potranno essere utilizzati per fini di marketing o personalizzazione dei contenuti, previa comunicazione e ottenimento del consenso dell'utente.\n" +
                "6. Condivisione dei Dati con Terze Parti\n" +
                "Attualmente, Trip4Pet non condivide i dati personali degli utenti con terze parti. Tuttavia, in base alla crescita dell'app, ci riserviamo la possibilità di condividere i dati con partner selezionati, previa notifica e ottenimento del consenso, come richiesto dalla normativa.\n" +
                "7. Conservazione dei Dati\n" +
                "I dati personali sono conservati per un periodo di 36 mesi dall'ultimo accesso all’app. Al termine, i dati verranno cancellati o anonimizzati, salvo diverse disposizioni legali.\n" +
                "8. Diritti dell'Utente\n" +
                "L'Utente ha il diritto di:\n" +
                "•\tAccedere ai propri dati e modificarli (compresi password e dati dell'animale);\n" +
                "•\tRichiedere una copia dei dati;\n" +
                "•\tRevocare il consenso al trattamento, con possibile limitazione dell'uso dell'app.\n" +
                "Nota: Le recensioni e i luoghi inseriti dagli utenti non potranno essere cancellati, in quanto contribuiscono al supporto della comunità di Trip4Pet.\n" +
                "9. Obblighi dell’Utente\n" +
                "L’utente si impegna a:\n" +
                "•\tUtilizzare l’app in modo responsabile e rispettoso delle finalità per cui è stata creata;\n" +
                "•\tNon consentire l’uso non autorizzato dell'app;\n" +
                "•\tManlevare Trip4Pet da qualsiasi danno causato dall'uso improprio dell'app.\n" +
                "10. Sicurezza dei Dati\n" +
                "Trip4Pet adotta misure di sicurezza per proteggere i dati da accessi non autorizzati o divulgazioni non consentite. Tuttavia, nessuna misura di sicurezza è infallibile e non possiamo garantire la completa protezione dei dati.\n" +
                "11. Esclusione di Garanzia\n" +
                "Trip4Pet fornisce l’app e i servizi \"così come sono\". Non garantiamo che i servizi saranno sempre disponibili, privi di errori o che risponderanno alle esigenze specifiche dell'utente.\n" +
                "12. Limitazione di Responsabilità\n" +
                "Trip4Pet non è responsabile per danni diretti, indiretti o incidentali derivanti dall'uso o dal mancato uso dell'app, incluse perdite di dati, mancato accesso o altre problematiche tecniche.\n" +
                "13. Modifiche al Contratto\n" +
                "Trip4Pet si riserva il diritto di modificare il presente contratto in qualsiasi momento. Gli utenti saranno informati delle modifiche tramite notifica. Continuando a utilizzare l'app, l’utente accetta i nuovi termini.\n" +
                "14. Risoluzione del Contratto\n" +
                "L'utente può interrompere l'uso dell'app in qualsiasi momento eliminandola dal proprio dispositivo. Trip4Pet può interrompere il servizio per motivi di sicurezza o per violazioni del presente contratto.\n" +
                "15. Legge Applicabile e Foro Competente\n" +
                "Questo contratto è regolato dalla legge italiana. In caso di controversie, il foro competente sarà esclusivamente quello di Nuoro.\n" +
                "16. Contatti\n" +
                "Per domande o chiarimenti, l’utente può contattare Trip4Pet all’indirizzo email: sinzos76@gmail.com\n";


        new NetworkTask().execute();

    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect("https://docs.google.com/document/d/e/2PACX-1vQBBalryFMlmEjLg3oVeNGbC7eqWXeEIGzGHOgGYzjOd1bTNQBtmEIw8Jc91obslirdHKHky7T5JWXy/pub").get();
                String text = document.text();
                Log.d(TAG, "Entire page text: " + text);

                Element contentElement = document.getElementById("contents");
                if (contentElement != null) {
                    String contentText = contentElement.text();
                    return contentText;
                } else {
                    Log.d(TAG, "Element with id 'content' not found.");
                    return "";
                }
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: " + e.getLocalizedMessage());
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d(TAG, "onPostExecute: " + result);
                // Update UI with the text result
                binding.contract.setText(result);
            }
        }
    }

    private static final String TAG = "ContractActivity";
}