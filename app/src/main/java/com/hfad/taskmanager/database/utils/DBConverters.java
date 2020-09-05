package com.hfad.taskmanager.database.utils;

import androidx.room.TypeConverter;

import com.hfad.taskmanager.model.State;

import java.util.Date;
import java.util.UUID;

public class DBConverters {

    //convert for UUID
    public static class UUIDConverter {
        @TypeConverter
        public String UUIDToString(UUID value) {
            return value.toString();
        }

        @TypeConverter
        public UUID fromString(String value) {
            return UUID.fromString(value);
        }
    }

    // example converter for java.util.Date
    public static class DateConverters {
        @TypeConverter
        public Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public Long dateToTimestamp(Date date) {
            if (date == null) {
                return null;
            } else {
                return date.getTime();
            }
        }
    }

    //Enum converter
    public static class EnumConverter {
        @TypeConverter
        public int stateToInt(State state) {
            return state.ordinal();
        }

        @TypeConverter
        public State fromInt(int value) {
            switch (value) {
                case 0:
                    return State.Todo;
                case 1:
                    return State.Doing;
                default:
                    return State.Done;
            }
        }
    }
}
