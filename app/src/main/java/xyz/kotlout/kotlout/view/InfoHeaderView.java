package xyz.kotlout.kotlout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.ExperimentController.ExperimentControllerObserver;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.Experiment;

/**
 * Custom View for the Experiment Info Fragment's header
 */
public class InfoHeaderView extends LinearLayout implements ExperimentControllerObserver {

  private final TextView tvDescription;
  private final TextView tvOwner;
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
    tvOwner = findViewById(R.id.header_owner);
    tvRegion = findViewById(R.id.header_region);
    tvType = findViewById(R.id.header_type);
    tvCount = findViewById(R.id.header_count);
  }


  public void setExperiment(String experimentId, ExperimentType type) {
    controller = new ExperimentController(experimentId, this);
    tvType.setText(type.toString());
  }

  @Override
  public void onExperimentLoaded() {
    Experiment experiment = controller.getExperimentContext();
    tvDescription.setText(experiment.getDescription());

    UserController ownerController = new UserController(experiment.getOwnerUuid());

    ownerController.setUpdateCallback((owner) -> {
      tvOwner.setText(owner.getDisplayName());
    });

    tvRegion.setText(experiment.getRegion());
    tvCount.setText(controller.generateCountText());
  }
}