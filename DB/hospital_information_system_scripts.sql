CREATE TABLE "employee"(
                           "employee_id" BIGINT NOT NULL,
                           "surname" VARCHAR(255) NOT NULL,
                           "first_name" VARCHAR(255) NOT NULL,
                           "address" VARCHAR(255) NOT NULL,
                           "telephone_nr" VARCHAR(255) NOT NULL,
                           "isActive" BOOLEAN NOT NULL,
                           "createdAt" TIMESTAMP NOT NULL,
                           "role" VARCHAR(255) NOT NULL
);

ALTER TABLE
    "employee" ADD PRIMARY KEY("employee_id");

ALTER TABLE
    "employee" ADD CONSTRAINT "employee_telephone_nr_unique" UNIQUE("telephone_nr");

CREATE TABLE "doctor"(
                         "doctor_id" BIGINT NOT NULL,
                         "employee_id" BIGINT NOT NULL,
                         "specialty" VARCHAR(255) NOT NULL
);

ALTER TABLE
    "doctor" ADD PRIMARY KEY("doctor_id");

CREATE TABLE "nurse"(
                        "nurse_id" BIGINT NOT NULL,
                        "employee_id" BIGINT NOT NULL,
                        "rotation" VARCHAR(255) NOT NULL,
                        "salary" DOUBLE PRECISION NOT NULL,
                        "department_id" BIGINT NOT NULL
);

ALTER TABLE
    "nurse" ADD PRIMARY KEY("nurse_id");

CREATE TABLE "department"(
                             "department_code" BIGINT NOT NULL,
                             "name" VARCHAR(255) NOT NULL,
                             "building" VARCHAR(255) NOT NULL,
                             "director_id" BIGINT NOT NULL,
                             "createdAt" TIMESTAMP NOT NULL
);

ALTER TABLE
    "department" ADD PRIMARY KEY("department_code");

CREATE TABLE "ward"(
                       "ward_id" BIGINT NOT NULL,
                       "ward_nr" BIGINT NOT NULL,
                       "nr_of_beds" INTEGER NOT NULL,
                       "department_id" BIGINT NOT NULL,
                       "supervisor_id" BIGINT NOT NULL,
                       "createdAt" TIMESTAMP NOT NULL
);

ALTER TABLE
    "ward" ADD PRIMARY KEY("ward_id");

CREATE TABLE "patient"(
                          "patient_id" BIGINT NOT NULL,
                          "surname" VARCHAR(255) NOT NULL,
                          "first_name" VARCHAR(255) NOT NULL,
                          "address" VARCHAR(255) NOT NULL,
                          "telephone_nr" VARCHAR(255) NOT NULL,
                          "createdAt" TIMESTAMP NOT NULL
);

ALTER TABLE
    "patient" ADD PRIMARY KEY("patient_id");

ALTER TABLE
    "patient" ADD CONSTRAINT "patient_telephone_nr_unique" UNIQUE("telephone_nr");

CREATE TABLE "hospitalization"(
                                  "hospitalization_id" BIGINT NOT NULL,
                                  "doctor_id" BIGINT NOT NULL,
                                  "patient_id" BIGINT NOT NULL,
                                  "ward_id" BIGINT NOT NULL,
                                  "bed_nr" INTEGER NOT NULL,
                                  "diagnosis" VARCHAR(255) NOT NULL,
                                  "description" VARCHAR(255) NOT NULL,
                                  "admission_date" TIMESTAMP NOT NULL,
                                  "discharge_date" TIMESTAMP NULL
);

ALTER TABLE
    "hospitalization" ADD PRIMARY KEY("hospitalization_id");

CREATE TABLE "transfer"(
                           "transfer_id" BIGINT NOT NULL,
                           "from_hospitalization_id" BIGINT NOT NULL,
                           "to_hospitalization_id" BIGINT NOT NULL,
                           "authorized_by" BIGINT NOT NULL,
                           "transfer_date" TIMESTAMP NOT NULL,
                           "transfer_reason" VARCHAR(255) NOT NULL
);

ALTER TABLE
    "transfer" ADD PRIMARY KEY("transfer_id");

-- Relationship : Creating Foreign keys

-- enforce uniqueness within the department
ALTER TABLE
    "ward" ADD CONSTRAINT unique_ward_nr_per_department UNIQUE ("ward_nr", "department_id");

-- enforce the amount of bed within a ward
ALTER TABLE
    "ward" ADD CONSTRAINT chk_nr_of_beds_range CHECK ("nr_of_beds" BETWEEN 0 AND 5);

ALTER TABLE
    "ward" ADD CONSTRAINT "ward_department_id_foreign" FOREIGN KEY("department_id") REFERENCES "department"("department_code");

ALTER TABLE
    "nurse" ADD CONSTRAINT "nurse_department_id_foreign" FOREIGN KEY("department_id") REFERENCES "department"("department_code");

ALTER TABLE
    "hospitalization" ADD CONSTRAINT "hospitalization_patient_id_foreign" FOREIGN KEY("patient_id") REFERENCES "patient"("patient_id");

ALTER TABLE
    "nurse" ADD CONSTRAINT "nurse_employee_id_foreign" FOREIGN KEY("employee_id") REFERENCES "employee"("employee_id");

ALTER TABLE
    "transfer" ADD CONSTRAINT "transfer_authorized_by_foreign" FOREIGN KEY("authorized_by") REFERENCES "doctor"("doctor_id");

ALTER TABLE
    "transfer" ADD CONSTRAINT "transfer_to_hospitalization_id_foreign" FOREIGN KEY("to_hospitalization_id") REFERENCES "hospitalization"("hospitalization_id");

ALTER TABLE
    "hospitalization" ADD CONSTRAINT "hospitalization_doctor_id_foreign" FOREIGN KEY("doctor_id") REFERENCES "doctor"("doctor_id");

ALTER TABLE
    "transfer" ADD CONSTRAINT "transfer_from_hospitalization_id_foreign" FOREIGN KEY("from_hospitalization_id") REFERENCES "hospitalization"("hospitalization_id");

ALTER TABLE
    "hospitalization" ADD CONSTRAINT "hospitalization_ward_id_foreign" FOREIGN KEY ("ward_id") REFERENCES "ward"("ward_id");

ALTER TABLE
    "ward" ADD CONSTRAINT "ward_supervisor_id_foreign" FOREIGN KEY("supervisor_id") REFERENCES "nurse"("nurse_id");

ALTER TABLE
    "doctor" ADD CONSTRAINT "doctor_employee_id_foreign" FOREIGN KEY("employee_id") REFERENCES "employee"("employee_id");

ALTER TABLE
    "department" ADD CONSTRAINT "department_director_id_foreign" FOREIGN KEY("director_id") REFERENCES "doctor"("doctor_id");

