package edu.gatech.seclass.groupimplementation.model.studio;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import edu.gatech.seclass.groupimplementation.model.event.Event;


public class StudioEventLine {
    @Embedded public Studio studio;
    @Relation(
            parentColumn = "studioShortName",
            entityColumn = "eventStudioOwner"
    )
    public List<Event> events;
}
