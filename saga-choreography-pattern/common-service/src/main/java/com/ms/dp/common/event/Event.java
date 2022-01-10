package com.ms.dp.common.event;

import java.time.LocalDate;
import java.util.UUID;

public interface Event {
	
	public UUID getEventId();
	public LocalDate getEventDate();

}
