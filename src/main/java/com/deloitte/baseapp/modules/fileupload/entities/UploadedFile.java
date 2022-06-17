package com.deloitte.baseapp.modules.fileupload.entities;
import com.deloitte.baseapp.commons.GenericEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "uploaded_file")
public class UploadedFile implements Serializable, GenericEntity<UploadedFile>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // data: raw data of the uploaded file
    @Column(name = "uf_data", nullable = false, length = 100000)
    private byte[] data;

    @Column(name="uf_file_size_kb")
    private double fileSizeKB;

    @Column(name="uf_filename")
    private String filename;

    @Column(name="uf_type")
    private String type;

    // origin: which form uploaded the file
    @Column(name="uf_origin")
    private String origin;

    @Override
    public void update(UploadedFile source) {
        this.data = source.getData();
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public UploadedFile createNewInstance() {
        UploadedFile newInstance = new UploadedFile(null, null, 0, null, null, null);
        newInstance.update(this);

        return newInstance;
    }
}

