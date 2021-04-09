package xyz.kotlout.kotlout.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.ExperimentController.ExperimentLoadedObserver;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.Experiment;

/**
 * Custom View for the Experiment Info Fragment's header.
 * <p>
 * Shows the description of the experiment, the owner of the experiment, region of experiment, type of experiment and the count
 * text.
 * <p>
 * setExperiment should be called as soon as possible to allow the header to load an experiment that it will pull the data
 * from.
 */
public class InfoHeaderView extends LinearLayout implements ExperimentLoadedObserver {

  private final TextView tvDescription;
  private final NameView nvOwner;
  private final TextView tvRegion;
  private final TextView tvType;
  private final TextView tvCount;

  private ExperimentController controller;

  public InfoHeaderView(Context context) {
    this(context, null);
  }

  public InfoHeaderView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public InfoHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    LayoutInflater inflater = LayoutInflater.from(context);
    inflater.inflate(R.layout.view_info_header, this, true);

    tvDescription = findViewById(R.id.header_description);
    nvOwner = findViewById(R.id.header_owner);
    tvRegion = findViewById(R.id.header_region);
    tvType = findViewById(R.id.header_type);
    tvCount = findViewById(R.id.header_count);
  }


  @SuppressLint("SetTextI18n")
  public void setExperiment(String experimentId, ExperimentType type) {
    controller = new ExperimentController(experimentId, this, null);
    switch (type) {
      case BINOMIAL:
        tvType.setText("Binomial");
        break;
      case NON_NEGATIVE_INTEGER:
        tvType.setText("Non-negative Integer");
        break;
      case COUNT:
        tvType.setText("Count");
        break;
      case MEASUREMENT:
        tvType.setText("Measurement");
        break;
    }
  }

  @Override
  public void onExperimentLoaded() {
    Experiment experiment = controller.getExperimentContext();
    tvDescription.setText(experiment.getDescription());

    UserController ownerController = new UserController(experiment.getOwnerUuid());

    ownerController.setUpdateCallback(nvOwner::setUser);

    tvRegion.setText(experiment.getRegion());
    tvCount.setText(controller.generateCountText());
  }
}