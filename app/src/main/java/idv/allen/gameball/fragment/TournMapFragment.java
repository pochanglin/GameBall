package idv.allen.gameball.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import idv.allen.gameball.R;
import idv.allen.gameball.tournment.TournamentVO;

public class TournMapFragment extends Fragment implements OnMapReadyCallback {
    private Bundle bundle;
    private TournamentVO tournamentVO;

    public TournMapFragment() {
        super();
    }
    public void setData(Bundle bundle) {
        this.bundle = bundle;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tourn_map,container,false);
        String tournamentVOJSON = bundle.getString("tournamentVOJSON");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        tournamentVO = gson.fromJson(tournamentVOJSON,TournamentVO.class);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.fmMap);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng position = new LatLng(Double.parseDouble(tournamentVO.getLoc_latitude()),
                                            Double.parseDouble(tournamentVO.getLoc_longitude()));
        googleMap.setMyLocationEnabled(true);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));

        googleMap.addMarker(new MarkerOptions()
                .title(tournamentVO.getTourn_name())
                .snippet(tournamentVO.getTourn_address())
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
    }
}
