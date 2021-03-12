package xyz.kotlout.kotlout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.model.experiment.Experiment;

/**
 * Custom View for the Experiment Info Fragment's header
 */
public class InfoHeaderView extends LinearLayout {

  private final TextView tvDescription;
  private final TextView tvOwner;
  private final TextView tvRegion;
  private final TextView tvType;
  private final TextView tvCount;

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

    tvDescription = (TextView) findViewById(R.id.header_description);
    tvOwner = (TextView) findViewById(R.id.header_owner);
    tvRegion = (TextView) findViewById(R.id.header_region);
    tvType = (TextView) findViewById(R.id.header_type);
    tvCount = (TextView) findViewById(R.id.header_count);
  }


  public void setExperiment(Experiment experiment) {
    tvDescription.setText(experiment.getDescription());

    String ownerName = experiment.getOwner().getFirstName() + experiment.getOwner().getLastName();
    tvOwner.setText(ownerName.toUpperCase());

    tvRegion.setText(experiment.getRegion());
//    tvType.setText(experiment.getType());
    tvType.setText("Binomial");

    ExperimentController exp = new ExperimentController(experiment);
    tvCount.setText(exp.generateCountText());

  }
}